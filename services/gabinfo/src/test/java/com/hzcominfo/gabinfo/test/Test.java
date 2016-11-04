/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabinfo.test;

import com.hzcominfo.gabinfo.conf.GABInfoConfiguration;
import com.hzcominfo.gabinfo.dao.GABInfoDao;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hzcominfo.gabinfo.pojo.GABInfoInput;

/**
 *
 * @author cominfo4
 */
public class Test {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Test.class);

        Map<String, String> condition = new HashMap<>();
        condition.put("SFZH", "330103199107170010");

        GABInfoInput input = new GABInfoInput();
        input.setServerId(GABInfoConfiguration.SERVER_ID_QGRK);
        input.setUserCardId("330103199107170010");
        input.setUserName("华晓阳");
        input.setUserDept("330100230500");
        input.setDataObjectCode("A001");//A090  A001
        input.setFields(new String[0]);

        input.setCondition(condition);

        long startTime = System.currentTimeMillis();

        Map<String, String> result = null;
        try {
            result = new GABInfoDao().fetchRow(input);
        } catch (Exception e) {
            logger.error(null, e);
        }
        if (result != null) {
            String CSDStr = result.get("CSD");
            if (CSDStr != null && CSDStr.contains("/")) {
                String[] csdStrs = CSDStr.split("/");
                CSDStr = (csdStrs.length == 2) ? csdStrs[1] : CSDStr.substring(0, CSDStr.indexOf("/"));
                result.put("CSD", CSDStr);
            }
            result.remove("XP");
        }

        logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- GABInfo Query -- ").append("QGRK").append(" -- spend time --> ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

        logger.info(" -------> " + result);

    }

}
