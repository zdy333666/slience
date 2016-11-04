/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.service;

import com.hzcominfo.rchl.conf.MongoSource;
import com.hzcominfo.rchl.conf.MongoSourceFactory;
import com.hzcominfo.rchl.conf.RCHLConfiguration;
import com.hzcominfo.rchl.dao.JCZCodeDao;
import com.hzcominfo.rchl.dao.RCHL_JCZCountDao;
import com.hzcominfo.rchl.pojo.CountInput;
import com.hzcominfo.rchl.pojo.JCZCount;
import com.mongodb.DB;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cominfo4
 */
@Service
public class RCHL_JCZCountService {

    private final Logger logger = LoggerFactory.getLogger(RCHL_JCZCountService.class);

    @Autowired
    private JCZCodeDao _JCZCodeDao;

    @Autowired
    private RCHL_JCZCountDao _RCHL_JCZCountDao;

    /**
     *
     * @param input
     * @return
     */
    public List<JCZCount> countOfJCZ(CountInput input) {

        List<JCZCount> result = new LinkedList<>();

        int isZero = input.getIsZero();
        String cjbmcode = input.getCjbmcode();

        List<JCZCount> personCountResult = personCountOfJCZ(input);
        List<JCZCount> carCountResult = carCountOfJCZ(input);

        logger.debug(" personCountResult size ---> " + personCountResult.size());
        logger.debug(" carCountResult size ---> " + carCountResult.size());

        Map<String, JCZCount> bmcode_carCountResult = new HashMap<>();
        for (JCZCount carCount : carCountResult) {
            bmcode_carCountResult.put(carCount.getBmcode(), carCount);
        }

        int sum_person_countH0 = 0;
        int sum_person_countH1 = 0;
        int sum_person_countH2 = 0;
        int sum_person_countH3 = 0;
        int sum_person_countH4 = 0;

        int sum_car_countH0 = 0;
        int sum_car_countH1 = 0;
        int sum_car_countH2 = 0;
        int sum_car_countH3 = 0;
        int sum_car_countH4 = 0;

        for (JCZCount personCount : personCountResult) {
            String bmcode = personCount.getBmcode();

            int total = personCount.getInspectpersonCountH();
            if (bmcode_carCountResult.containsKey(bmcode)) {
                JCZCount carCount = bmcode_carCountResult.get(bmcode);

                personCount.setInspectcarCountH(carCount.getInspectcarCountH());
                personCount.setInspectcarCountH0(carCount.getInspectcarCountH0());
                personCount.setInspectcarCountH1(carCount.getInspectcarCountH1());
                personCount.setInspectcarCountH2(carCount.getInspectcarCountH2());
                personCount.setInspectcarCountH3(carCount.getInspectcarCountH3());
                personCount.setInspectcarCountH4(carCount.getInspectcarCountH4());

                total += carCount.getInspectcarCountH();
            }

            if (isZero == 0 && total == 0) {
                continue;
            }

            result.add(personCount);

            sum_person_countH0 += personCount.getInspectpersonCountH0();
            sum_person_countH1 += personCount.getInspectpersonCountH1();
            sum_person_countH2 += personCount.getInspectpersonCountH2();
            sum_person_countH3 += personCount.getInspectpersonCountH3();
            sum_person_countH4 += personCount.getInspectpersonCountH4();

            sum_car_countH0 += personCount.getInspectcarCountH0();
            sum_car_countH1 += personCount.getInspectcarCountH1();
            sum_car_countH2 += personCount.getInspectcarCountH2();
            sum_car_countH3 += personCount.getInspectcarCountH3();
            sum_car_countH4 += personCount.getInspectcarCountH4();

        }

        Collections.sort(result, new CountComparator());

        JCZCount jczCount = new JCZCount();
        jczCount.setBmcode(cjbmcode == null ? "*" : cjbmcode);
        jczCount.setBm("总计");
        jczCount.setInspectpersonCountH(sum_person_countH0 + sum_person_countH1 + sum_person_countH2 + sum_person_countH3 + sum_person_countH4);
        jczCount.setInspectpersonCountH0(sum_person_countH0);
        jczCount.setInspectpersonCountH1(sum_person_countH1);
        jczCount.setInspectpersonCountH2(sum_person_countH2);
        jczCount.setInspectpersonCountH3(sum_person_countH3);
        jczCount.setInspectpersonCountH4(sum_person_countH4);
        jczCount.setInspectcarCountH(sum_car_countH0 + sum_car_countH1 + sum_car_countH2 + sum_car_countH3 + sum_car_countH4);
        jczCount.setInspectcarCountH0(sum_car_countH0);
        jczCount.setInspectcarCountH1(sum_car_countH1);
        jczCount.setInspectcarCountH2(sum_car_countH2);
        jczCount.setInspectcarCountH3(sum_car_countH3);
        jczCount.setInspectcarCountH4(sum_car_countH4);

        result.add(jczCount);

        return result;

    }

    /**
     *
     */
    class CountComparator implements Comparator<JCZCount> {

        @Override
        public int compare(JCZCount o1, JCZCount o2) {
            return o2.getInspectpersonCountH() - o1.getInspectpersonCountH();
        }
    }

    /**
     * 人员 核录统计
     *
     * @param input
     * @return
     */
    public List<JCZCount> personCountOfJCZ(CountInput input) {

        List<JCZCount> result = new LinkedList<>();

        String cjbmcode = input.getCjbmcode();
        //int isZero = input.getIsZero();
        String startTime = input.getStartTime();
        String endTime = input.getEndTime();

        try {
            MongoSource source = MongoSourceFactory.getSource(RCHLConfiguration.mongo_uri_hzga);
            DB db = source.getClient().getDB(source.getClientURI().getDatabase());

            //获取检查站的信息列表
            Map<String, Map<String, String>> dptInfos = _JCZCodeDao.queryAll(db);
            if (dptInfos.isEmpty()) {
                return result;
            }

            logger.debug(" dptInfos ---> " + dptInfos);
            long checkTime = System.currentTimeMillis();

            //获取统计信息列表
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Map<String, Object> params = new HashMap<>();
            params.put("SJLY_s", input.getFlag());
            params.put("HCBB_s", input.getYycj());
            params.put("HCJG_s", input.getReturncode());
            if (!(cjbmcode == null || cjbmcode.trim().isEmpty() || "*".equals(cjbmcode))) {
                params.put("HCDWJGDM_s", cjbmcode + "*");
            }
            params.put("HCSJ_time_l", "[" + (startTime == null ? "*" : dateFormat.parse(startTime).getTime()) + " TO " + (endTime == null ? "*" : dateFormat.parse(endTime).getTime()) + "]");

            List<Map<String, Object>> inspectPersonCountInfos = _RCHL_JCZCountDao.count(RCHLConfiguration.solr_uri_ryhc, params);

            logger.debug(" personCountInfos ---> " + inspectPersonCountInfos.size());
            logger.debug(" Get personCountInfos spend time ---> " + (System.currentTimeMillis() - checkTime) + " ms");

            Map<String, Map<String, Object>> bmcode_inspectPersonCountInfos = new HashMap<>();
            for (Map<String, Object> m : inspectPersonCountInfos) {
                bmcode_inspectPersonCountInfos.put((String) m.get("value"), m);
            }

            for (Entry<String, Map<String, String>> dptInfo : dptInfos.entrySet()) {
                String bmcode = dptInfo.getKey();

                JCZCount personCount = new JCZCount();
                personCount.setBmcode(bmcode);

                int countH0 = 0;
                int countH1 = 0;
                int countH2 = 0;
                int countH3 = 0;
                int countH4 = 0;
                if (bmcode_inspectPersonCountInfos.containsKey(bmcode)) {
                    Map<String, Object> row = bmcode_inspectPersonCountInfos.get(bmcode);

                    List<Map<String, Object>> items = (List<Map<String, Object>>) row.get("items");
                    for (Map<String, Object> item : items) {
                        String value = (String) item.get("value");
                        int count = (int) item.get("count");

                        if ("0".equals(value)) {
                            countH0 += count;
                        } else if ("1".equals(value)) {
                            countH1 += count;
                        } else if ("2".equals(value)) {
                            countH2 += count;
                        } else if ("3".equals(value)) {
                            countH3 += count;
                        } else if ("4".equals(value)) {
                            countH4 += count;
                        }
                    }
                }

                int total = countH0 + countH1 + countH2 + countH3 + countH4;

                Map<String, String> codeItem = dptInfo.getValue();

                personCount.setInspectpersonCountH(total);
                personCount.setBm(codeItem.get("bm"));
                personCount.setInspectpersonCountH(total);
                personCount.setInspectpersonCountH0(countH0);
                personCount.setInspectpersonCountH1(countH1);
                personCount.setInspectpersonCountH2(countH2);
                personCount.setInspectpersonCountH3(countH3);
                personCount.setInspectpersonCountH4(countH4);

                result.add(personCount);
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        return result;
    }

    /**
     * 车辆核录统计
     *
     * @param input
     * @return
     */
    public List<JCZCount> carCountOfJCZ(CountInput input) {

        List<JCZCount> result = new LinkedList<>();

        String cjbmcode = input.getCjbmcode();
        //int isZero = input.getIsZero();
        String startTime = input.getStartTime();
        String endTime = input.getEndTime();

        try {
            MongoSource source = MongoSourceFactory.getSource(RCHLConfiguration.mongo_uri_hzga);
            DB db = source.getClient().getDB(source.getClientURI().getDatabase());

            //获取当前检查站的信息列表
            Map<String, Map<String, String>> dptInfos = _JCZCodeDao.queryAll(db);
            if (dptInfos.isEmpty()) {
                return result;
            }

            logger.debug(" dptInfos ---> " + dptInfos);
            long checkTime = System.currentTimeMillis();

            //获取统计信息列表
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat_l = new SimpleDateFormat("yyyyMMddHHmmss");

            Map<String, Object> params = new HashMap<>();
            params.put("SJLY_s", input.getFlag());
            params.put("HCBB_s", input.getYycj());
            params.put("HCJG_s", input.getReturncode());
            if (!(cjbmcode == null || cjbmcode.trim().isEmpty() || "*".equals(cjbmcode))) {
                params.put("HCDWJGDM_s", cjbmcode + "*");
            }
            params.put("HCSJ_l", "[" + (startTime == null ? "*" : Long.valueOf(dateFormat_l.format(dateFormat.parse(startTime)))) + " TO " + (endTime == null ? "*" : Long.valueOf(dateFormat_l.format(dateFormat.parse(endTime)))) + "]");

            List<Map<String, Object>> inspectCarCountInfos = _RCHL_JCZCountDao.count(RCHLConfiguration.solr_uri_clhc, params);

            logger.debug(" carCountInfos ---> " + inspectCarCountInfos.size());
            logger.debug(" Get carCountInfos spend time ---> " + (System.currentTimeMillis() - checkTime) + " ms");

            Map<String, Map<String, Object>> bmcode_inspectCarCountInfos = new HashMap<>();
            for (Map<String, Object> m : inspectCarCountInfos) {
                bmcode_inspectCarCountInfos.put((String) m.get("value"), m);
            }

            for (Entry<String, Map<String, String>> dptInfo : dptInfos.entrySet()) {
                String bmcode = dptInfo.getKey();

                JCZCount carCount = new JCZCount();
                carCount.setBmcode(bmcode);

                int countH0 = 0;
                int countH1 = 0;
                int countH2 = 0;
                int countH3 = 0;
                int countH4 = 0;
                if (bmcode_inspectCarCountInfos.containsKey(bmcode)) {
                    Map<String, Object> row = bmcode_inspectCarCountInfos.get(bmcode);

                    List<Map<String, Object>> items = (List<Map<String, Object>>) row.get("items");
                    for (Map<String, Object> item : items) {
                        String value = (String) item.get("value");
                        int count = (int) item.get("count");

                        if ("0".equals(value)) {
                            countH0 += count;
                        } else if ("1".equals(value)) {
                            countH1 += count;
                        } else if ("2".equals(value)) {
                            countH2 += count;
                        } else if ("3".equals(value)) {
                            countH3 += count;
                        } else if ("4".equals(value)) {
                            countH4 += count;
                        }
                    }
                }

                int total = countH0 + countH1 + countH2 + countH3 + countH4;

                Map<String, String> codeItem = dptInfo.getValue();

                carCount.setInspectcarCountH(total);
                carCount.setBm(codeItem.get("bm"));
                carCount.setInspectcarCountH(total);
                carCount.setInspectcarCountH0(countH0);
                carCount.setInspectcarCountH1(countH1);
                carCount.setInspectcarCountH2(countH2);
                carCount.setInspectcarCountH3(countH3);
                carCount.setInspectcarCountH4(countH4);

                result.add(carCount);
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        return result;
    }

}
