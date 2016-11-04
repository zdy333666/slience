/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.service;

import com.hzcominfo.relation.conf.MongoSource;
import com.hzcominfo.relation.conf.MongoSourceFactory;
import com.hzcominfo.relation.conf.Neo4jSourceFactory;
import com.hzcominfo.relation.conf.RelationConfiguration;
import com.hzcominfo.relation.dao.RelationBasicDao;
import com.hzcominfo.relation.dao.CommonRelationDao;
import com.hzcominfo.relation.dao.FlightNeighborRelationDetailHandler;
import com.hzcominfo.relation.dao.PeccancyProcessRelationDetailHandler;
import com.hzcominfo.relation.dao.PhoneTransRelationDetailHandler;
import com.hzcominfo.relation.dao.RelationDetailHandler;
import com.hzcominfo.relation.dao.SameCasePartyRelationDetailHandler;
import com.hzcominfo.relation.dao.SameCaseRelationDetailHandler;
import com.hzcominfo.relation.dao.SameContactRelationDetailHandler;
import com.hzcominfo.relation.dao.SameCybercafeRelationDetailHandler;
import com.hzcominfo.relation.dao.SameFlightRelationDetailHandler;
import com.hzcominfo.relation.dao.SameHotelRoomRelationDetailHandler;
import com.hzcominfo.relation.dao.SameHouseholdRelationDetailHandler;
import com.hzcominfo.relation.dao.SamePrisonRoomRelationDetailHandler;
import com.hzcominfo.relation.dao.SameStayRelationDetailHandler;
import com.hzcominfo.relation.dao.SameUnitRelationDetailHandler;
import com.hzcominfo.relation.pojo.PeopleCriminalStatus;
import com.hzcominfo.relation.pojo.RelationDefine;
import com.hzcominfo.relation.pojo.RelationDetail;
import com.mongodb.DBCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.neo4j.driver.v1.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author cominfo4
 */
@Service
public class SinglePersonRelationService {

    private final Logger logger = LoggerFactory.getLogger(SinglePersonRelationService.class);

    @Autowired
    private CommonRelationDao commonRelationDao;

    @Autowired
    private RelationBasicDao relationBasicDao;

    @LoadBalanced
    @Autowired
    private RestTemplate restTemplate;

    /**
     *
     */
    class ScoreComparator implements Comparator<Map<String, Object>> {

        @Override
        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
            return (int) o2.get("score") - (int) o1.get("score");

        }

    }

    /**
     *
     * @param sfzh
     * @return
     */
    public List<Map<String, Object>> fetchRelationsWithOrder(String sfzh) {

        return fetchRelationsWithOrder(sfzh, null);
    }

    /**
     *
     * @param sfzh
     * @return
     */
    public List<Map<String, Object>> fetchRelationsWithOrder(String sfzh, String[] types) {

        List<Map<String, Object>> result = new LinkedList<>();

        Session session = null;
        try {
            MongoSource rtdb_source = MongoSourceFactory.getSource(RelationConfiguration.mongo_uri_rtdb);
            DBCollection relation_define_coll = rtdb_source.getClient().getDB(rtdb_source.getClientURI().getDatabase()).getCollection(RelationConfiguration.relation_define_table);
            List<RelationDefine> relationDefines = relationBasicDao.getRelationDefines(relation_define_coll);

            Map<String, RelationDefine> type_relationDefines = new HashMap<>();
            for (RelationDefine relationDefine : relationDefines) {
                type_relationDefines.put(relationDefine.getName(), relationDefine);
            }

            session = (Neo4jSourceFactory.getSource(RelationConfiguration.neo4j_bolt_uri)).session();
            Map<String, List<String>> relations = commonRelationDao.fetchSinglePersonRelation(session, sfzh, types);
            session.close();

            MongoSource people_source = MongoSourceFactory.getSource(RelationConfiguration.mongo_uri_people);
            DBCollection base_people_coll = people_source.getClient().getDB(people_source.getClientURI().getDatabase()).getCollection(RelationConfiguration.people_base_table);
            DBCollection bad_people_coll = people_source.getClient().getDB(people_source.getClientURI().getDatabase()).getCollection(RelationConfiguration.people_bad_table);

            for (Entry<String, List<String>> entry : relations.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();

                int score = 0;
                Map<String, Integer> typeCounter = new TreeMap<>();
                Map<String, Integer> typeScore = new TreeMap<>();

                for (String type : value) {

                    if (!type_relationDefines.containsKey(type)) {
                        continue;
                    }

                    int weight = type_relationDefines.get(type).getWeight();
                    score += weight;

                    if (typeCounter.containsKey(type)) {
                        typeCounter.put(type, typeCounter.get(type) + 1);
                    } else {
                        typeCounter.put(type, 1);
                    }

                    if (typeScore.containsKey(type)) {
                        typeScore.put(type, typeScore.get(type) + weight);
                    } else {
                        typeScore.put(type, weight);
                    }
                }

                List<Map<String, Object>> items = new LinkedList<>();
                for (Entry<String, Integer> detail_entry : typeCounter.entrySet()) {
                    String type = detail_entry.getKey();

                    Map<String, Object> detailItem = new TreeMap<>();
                    detailItem.put("type", type);
                    detailItem.put("displayType", type_relationDefines.get(type).getDisplayName());
                    detailItem.put("count", detail_entry.getValue());
                    detailItem.put("score", typeScore.get(type));

                    items.add(detailItem);
                }

                String[] keys = key.split(":");
                String r_sfzh = keys[0];
                String r_xm = keys[1];

                String sex = r_sfzh.charAt(16) % 2 == 0 ? "女" : "男";
                String birthday = r_sfzh.substring(6, 14);

                Map<String, Object> row = new HashMap<>();
                row.put("sfzh", r_sfzh);
                row.put("xm", r_xm);
                row.put("sex", sex);
                row.put("birthday", birthday);
                row.put("score", score);
                row.put("relations", items);

                //---------------------------------
                Map<String, Object> peopleBaseInfo = relationBasicDao.getPeopleBaseInfo(base_people_coll, r_sfzh, new String[]{"XM", "JGD_FORMAT"});
                if (peopleBaseInfo != null) {
                    String b_xm = (String) peopleBaseInfo.get("XM");
                    if (!(b_xm == null || b_xm.trim().isEmpty())) {
                        row.put("xm", b_xm);
                    }

                    row.put("domicile", peopleBaseInfo.get("JGD_FORMAT"));
                }

                PeopleCriminalStatus status = relationBasicDao.checkPeopleCriminal(bad_people_coll, r_sfzh);
                row.put("criminal", status.isIsCriminal());
                row.put("criminal_drug", status.isIsCriminalDrug());
                //---------------------------------
                result.add(row);
            }

            Collections.sort(result, new ScoreComparator());

            //-------------------------------------------
            Map<String, Object> row = new HashMap<>();
            row.put("sfzh", sfzh);
            row.put("sex", sfzh.charAt(16) % 2 == 0 ? "女" : "男");
            row.put("birthday", sfzh.substring(6, 14));

            Map<String, Object> personInfo = null;

//                Map<String, String> condition = new HashMap<>();
//                condition.put("SFZH", sfzh);
//
//                Map<String, Object> input = new HashMap<>();
//                input.put("condition", condition);
//                input.put("fields", new String[]{"XM", "JGSSX"});
//                input.put("userCardId", "330103199107170010");
//                input.put("userName", "华晓阳");
//                input.put("userDept", "330100230500");
//
//              
//                try {
//                    personInfo = restTemplate.postForObject("http://gabinfo/QGRK", input, Map.class);
//                } catch (Exception ex) {
//                    logger.error("", ex);
//                }
            Map<String, Object> peopleBaseInfo = relationBasicDao.getPeopleBaseInfo(base_people_coll, sfzh, new String[]{"XM", "JGD_FORMAT"});
            if (peopleBaseInfo != null) {
                String b_xm = (String) peopleBaseInfo.get("XM");
                if (!(b_xm == null || b_xm.trim().isEmpty())) {
                    row.put("xm", b_xm);
                } else {
                    session = (Neo4jSourceFactory.getSource(RelationConfiguration.neo4j_bolt_uri)).session();
                    personInfo = commonRelationDao.fetchSinglePerson(session, sfzh);
                    session.close();
                    if (personInfo != null) {
                        row.put("xm", personInfo.get("XM"));
                    }
                }

                row.put("domicile", peopleBaseInfo.get("JGD_FORMAT"));
            }

            PeopleCriminalStatus status = relationBasicDao.checkPeopleCriminal(bad_people_coll, sfzh);
            row.put("criminal", status.isIsCriminal());
            row.put("criminal_drug", status.isIsCriminalDrug());

            result.add(0, row);

        } catch (Exception ex) {
            if (session != null && session.isOpen()) {
                session.close();
            }
            logger.error("", ex);
            return null;
        }

        return result;
    }

    /**
     *
     */
    public Collection<RelationDetail> fetchRelationDetail(String[] pair, String type) {

        Collection<RelationDetail> relationDetails = null;
        RelationDetailHandler handler = null;
        Session session = null;

        try {

            if ("SameHotelRoom".equals(type)) {
                handler = new SameHotelRoomRelationDetailHandler();
            } else if ("SameHousehold".equals(type)) {
                handler = new SameHouseholdRelationDetailHandler();
            } else if ("SameFlight".equals(type)) {
                handler = new SameFlightRelationDetailHandler();
            } else if ("FlightNeighbor".equals(type)) {
                handler = new FlightNeighborRelationDetailHandler();
            } else if ("SameStay".equals(type)) {
                handler = new SameStayRelationDetailHandler();
            } else if ("SameContact".equals(type)) {
                handler = new SameContactRelationDetailHandler();
            } else if ("SamePrisonRoom".equals(type)) {
                handler = new SamePrisonRoomRelationDetailHandler();
            } else if ("PeccancyProcess".equals(type)) {
                handler = new PeccancyProcessRelationDetailHandler();
            } else if ("PhoneTrans".equals(type)) {
                handler = new PhoneTransRelationDetailHandler();
            } else if ("SameUnit".equals(type)) {
                handler = new SameUnitRelationDetailHandler();
            } else if ("SameUnit100".equals(type)) {
                handler = new SameUnitRelationDetailHandler();
            } else if ("SameCase".equals(type)) {
                handler = new SameCaseRelationDetailHandler();
            } else if ("SameCybercafe".equals(type)) {
                handler = new SameCybercafeRelationDetailHandler();
            } else if ("SameCaseParty".equals(type)) {
                handler = new SameCasePartyRelationDetailHandler();
            }

            if (handler != null) {
                session = Neo4jSourceFactory.getSource(RelationConfiguration.neo4j_bolt_uri).session();
                relationDetails = handler.getDetail(session, pair, type);
                session.close();
            }

        } catch (Exception ex) {
            if (session != null && session.isOpen()) {
                session.close();
            }
            logger.error("", ex);
        }

        return relationDetails;
    }

}
