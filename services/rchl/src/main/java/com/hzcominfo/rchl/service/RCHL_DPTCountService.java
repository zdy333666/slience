/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.service;

import com.hzcominfo.rchl.conf.MongoSource;
import com.hzcominfo.rchl.conf.MongoSourceFactory;
import com.hzcominfo.rchl.conf.RCHLConfiguration;
import com.hzcominfo.rchl.dao.DPTCodeDao;
import com.hzcominfo.rchl.dao.RCHL_DTPCountDao;
import com.hzcominfo.rchl.pojo.CountInput;
import com.hzcominfo.rchl.pojo.DPTCarCount;
import com.hzcominfo.rchl.pojo.DPTPersonCount;
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
public class RCHL_DPTCountService {

    private final Logger logger = LoggerFactory.getLogger(RCHL_DPTCountService.class);

    @Autowired
    private DPTCodeDao _DPTCodeDao;

    @Autowired
    private RCHL_DTPCountDao inspectDTPCountDao;

    /**
     * 人员核录统计
     *
     * @param input
     * @return
     */
    public List<DPTPersonCount> personCountOfDPT(CountInput input) {

        List<DPTPersonCount> result = new LinkedList<>();

        String cjbmcode = input.getCjbmcode();
        int isZero = input.getIsZero();
        String startTime = input.getStartTime();
        String endTime = input.getEndTime();

        try {
            MongoSource source = MongoSourceFactory.getSource(RCHLConfiguration.mongo_uri_hzga);
            DB db = source.getClient().getDB(source.getClientURI().getDatabase());

            //获取当前部门的下级信息列表
            Map<String, Map<String, String>> dptInfos = _DPTCodeDao.queryItems(db, cjbmcode);
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

            List<Map<String, Object>> inspectPersonCountInfos = inspectDTPCountDao.count(RCHLConfiguration.solr_uri_ryhc, params, cjbmcode);

            logger.debug(" personCountInfos ---> " + inspectPersonCountInfos.size());
            logger.debug(" Get personCountInfos spend time ---> " + (System.currentTimeMillis() - checkTime) + " ms");

            Map<String, Map<String, Object>> bmcode_inspectPersonCountInfos = new HashMap<>();
            for (Map<String, Object> m : inspectPersonCountInfos) {
                bmcode_inspectPersonCountInfos.put((String) m.get("value"), m);
            }

            int sum_countH0 = 0;
            int sum_countH1 = 0;
            int sum_countH2 = 0;
            int sum_countH3 = 0;
            int sum_countH4 = 0;

            for (Entry<String, Map<String, String>> dptInfo : dptInfos.entrySet()) {
                String bmcode = dptInfo.getKey();

                DPTPersonCount personCount = new DPTPersonCount();
                personCount.setBmcode(bmcode);

                int countH0 = 0;
                int countH1 = 0;
                int countH2 = 0;
                int countH3 = 0;
                int countH4 = 0;
                for (String temp_bmcode : bmcode_inspectPersonCountInfos.keySet()) {
                    if (!temp_bmcode.startsWith(bmcode)) {
                        continue;
                    }

                    Map<String, Object> row = bmcode_inspectPersonCountInfos.get(temp_bmcode);

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
//                if (isZero == 0 && total == 0) {
//                    continue;
//                }

                Map<String, String> codeItem = dptInfo.getValue();

                personCount.setBm(codeItem.get("bm"));
                personCount.setInspectpersonCountH(total);
                personCount.setInspectpersonCountH0(countH0);
                personCount.setInspectpersonCountH1(countH1);
                personCount.setInspectpersonCountH2(countH2);
                personCount.setInspectpersonCountH3(countH3);
                personCount.setInspectpersonCountH4(countH4);

                result.add(personCount);

                sum_countH0 += countH0;
                sum_countH1 += countH1;
                sum_countH2 += countH2;
                sum_countH3 += countH3;
                sum_countH4 += countH4;
            }

            Collections.sort(result, new PersonCountComparator());

            int sum_total = sum_countH0 + sum_countH1 + sum_countH2 + sum_countH3 + sum_countH4;

            DPTPersonCount personCount = new DPTPersonCount();
            personCount.setBmcode(cjbmcode == null ? "*" : cjbmcode);
            personCount.setBm("总计");
            personCount.setInspectpersonCountH(sum_total);
            personCount.setInspectpersonCountH0(sum_countH0);
            personCount.setInspectpersonCountH1(sum_countH1);
            personCount.setInspectpersonCountH2(sum_countH2);
            personCount.setInspectpersonCountH3(sum_countH3);
            personCount.setInspectpersonCountH4(sum_countH4);

            result.add(personCount);

        } catch (Exception e) {
            logger.error("", e);
        }

        return result;
    }

    /**
     *
     */
    class PersonCountComparator implements Comparator<DPTPersonCount> {

        @Override
        public int compare(DPTPersonCount o1, DPTPersonCount o2) {
            return o2.getInspectpersonCountH() - o1.getInspectpersonCountH();
        }
    }

    /**
     * 车辆核录统计
     *
     * @param input
     * @return
     */
    public List<DPTCarCount> carCountOfDPT(CountInput input) {

        List<DPTCarCount> result = new LinkedList<>();

        String cjbmcode = input.getCjbmcode();
        int isZero = input.getIsZero();
        String startTime = input.getStartTime();
        String endTime = input.getEndTime();

        try {
            MongoSource source = MongoSourceFactory.getSource(RCHLConfiguration.mongo_uri_hzga);
            DB db = source.getClient().getDB(source.getClientURI().getDatabase());

            //获取当前部门的下级信息列表
            Map<String, Map<String, String>> dptInfos = _DPTCodeDao.queryItems(db, cjbmcode);
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

            List<Map<String, Object>> inspectCarCountInfos = inspectDTPCountDao.count(RCHLConfiguration.solr_uri_clhc, params, cjbmcode);

            logger.debug(" carCountInfos ---> " + inspectCarCountInfos.size());
            logger.debug(" Get carCountInfos spend time ---> " + (System.currentTimeMillis() - checkTime) + " ms");

            Map<String, Map<String, Object>> bmcode_inspectCarCountInfos = new HashMap<>();
            for (Map<String, Object> m : inspectCarCountInfos) {
                bmcode_inspectCarCountInfos.put((String) m.get("value"), m);
            }

            int sum_countH0 = 0;
            int sum_countH1 = 0;
            int sum_countH2 = 0;
            int sum_countH3 = 0;
            int sum_countH4 = 0;

            for (Entry<String, Map<String, String>> dptInfo : dptInfos.entrySet()) {
                String bmcode = dptInfo.getKey();

                DPTCarCount carCount = new DPTCarCount();
                carCount.setBmcode(bmcode);

                int countH0 = 0;
                int countH1 = 0;
                int countH2 = 0;
                int countH3 = 0;
                int countH4 = 0;
                for (String temp_bmcode : bmcode_inspectCarCountInfos.keySet()) {
                    if (!temp_bmcode.startsWith(bmcode)) {
                        continue;
                    }

                    Map<String, Object> row = bmcode_inspectCarCountInfos.get(temp_bmcode);
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
//                if (isZero == 0 && total == 0) {
//                    continue;
//                }

                Map<String, String> codeItem = dptInfo.getValue();

                carCount.setBm(codeItem.get("bm"));
                carCount.setInspectcarCountH(total);
                carCount.setInspectcarCountH0(countH0);
                carCount.setInspectcarCountH1(countH1);
                carCount.setInspectcarCountH2(countH2);
                carCount.setInspectcarCountH3(countH3);
                carCount.setInspectcarCountH4(countH4);

                result.add(carCount);

                sum_countH0 += countH0;
                sum_countH1 += countH1;
                sum_countH2 += countH2;
                sum_countH3 += countH3;
                sum_countH4 += countH4;
            }

            Collections.sort(result, new CarCountComparator());

            int sum_total = sum_countH0 + sum_countH1 + sum_countH2 + sum_countH3 + sum_countH4;

            DPTCarCount carCount = new DPTCarCount();
            carCount.setBmcode(cjbmcode == null ? "*" : cjbmcode);
            carCount.setBm("总计");
            carCount.setInspectcarCountH(sum_total);
            carCount.setInspectcarCountH0(sum_countH0);
            carCount.setInspectcarCountH1(sum_countH1);
            carCount.setInspectcarCountH2(sum_countH2);
            carCount.setInspectcarCountH3(sum_countH3);
            carCount.setInspectcarCountH4(sum_countH4);

            result.add(carCount);

        } catch (Exception e) {
            logger.error("", e);
        }

        return result;
    }

    /**
     *
     */
    class CarCountComparator implements Comparator<DPTCarCount> {

        @Override
        public int compare(DPTCarCount o1, DPTCarCount o2) {
            return o2.getInspectcarCountH() - o1.getInspectcarCountH();
        }
    }

}
