/*
 */
package com.hzcominfo.search.service;

import com.hzcominfo.search.pojo.SearchModelField;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author xzh
 */
public class SearchStats {

    public String fieldName;   //  字段名
    public String fieldPrefix; //  值前缀
    public int statsValueLen;  //  前缀后面的有效值长度
    public int sortType;   //  排序类型    1按值 2按统计数据的大小

    public static ArrayList< SearchStats> buildStatsParam(String paramStatsStr, HashMap< String, SearchModelField> paramMap1, HashMap< String, SearchModelField> paramMap2) {
        if ((paramStatsStr == null) || paramStatsStr.isEmpty()) {
            return null;
        }

        String[] localCmdList = paramStatsStr.split(",");
        if (localCmdList.length == 0) {
            return null;
        }
        
        ArrayList< SearchStats> localSearchStats = new ArrayList< SearchStats>();
        for (String tmpCmdStr : localCmdList) {
            String[] localStatsList = tmpCmdStr.split(":");
            if (localStatsList.length == 0) {
                continue;
            }
            
            String tmpStatsField = localStatsList[0];
            SearchStats localStats = new SearchStats();
            int localLoopFlag = 0;
            localStats.sortType = 1;
            //  处理前缀的+-
            do {
                localLoopFlag = 0;
                if (tmpStatsField.startsWith("+")) {
                    localLoopFlag = 1;
                    tmpStatsField = tmpStatsField.substring(1);
                    localStats.sortType = 1;
                } else if (tmpStatsField.startsWith("-")) {
                    tmpStatsField = tmpStatsField.substring(1);
                    localLoopFlag = 1;
                    localStats.sortType = 1;
                } else if (tmpStatsField.endsWith("+")) {
                    localLoopFlag = 2;
                    tmpStatsField = tmpStatsField.substring(0, tmpStatsField.length() - 1);
                    localStats.sortType = 1;
                } else if (tmpStatsField.endsWith("-")) {
                    tmpStatsField = tmpStatsField.substring(0, tmpStatsField.length() - 1);
                    localLoopFlag = 1;
                    localStats.sortType = 2;
                }
            } while (localLoopFlag != 0);
            
            SearchModelField localFieldDefine = paramMap1.get(tmpStatsField);
            if (localFieldDefine == null) {
                localFieldDefine = paramMap2.get(tmpStatsField);
            }
            if (localFieldDefine == null) {
                continue;
            }
            
            localStats.fieldName = localFieldDefine.getSourceField();
            if (localStats.fieldName.endsWith("_tcn")) {
                for (String tmpFieldName : localFieldDefine.getSourceFields()) {
                    if (tmpFieldName.endsWith("_s")) {
                        localStats.fieldName = tmpFieldName;
                        break;
                    }
                }
            }
            
            if (localStatsList.length >= 2) {
                localStats.fieldPrefix = localStatsList[1];
            }
            if (localStatsList.length >= 3) {
                try {
                    localStats.statsValueLen = Integer.parseInt(localStatsList[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            localSearchStats.add(localStats);
        }
        
        if (localSearchStats.isEmpty()) {
            return null;
        }
        return localSearchStats;
    }
}
