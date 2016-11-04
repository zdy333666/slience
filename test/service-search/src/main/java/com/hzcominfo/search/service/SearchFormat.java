/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

import com.hzcominfo.search.pojo.SearchModelField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TimeZone;

/**
 *
 * @author xzh
 */
public class SearchFormat {

    public HashMap< String, String> searchKeyFieldMap = new HashMap< String, String>(); //  要匹配的字段
    public String searchQ;  //  输出的查找字符串
    public ArrayList< String> searchWildList = new ArrayList< String>();  //  有通配符的字段
    public ArrayList< String> searchItemList = new ArrayList< String>(); //  要搜索的环节
    public HashSet< String> searchKeyFieldSet = new HashSet< String>();  //  未指定的匹配的值
    public static HashSet< String> keyWords = new HashSet< String>() {
        {
            add("*:*");
            add("AND");
            add("OR");
        }
    };

    public static boolean isChinese(String str) {
        for (int nIndex = 0; nIndex < str.length(); ++nIndex) {
            char ch = str.charAt(nIndex);
            if (ch > 0x80) {
                return true;
            }
        }
        return false;
    }

    public static String clearChinese(String str) {
        String localResult = "";
        for (int nIndex = 0; nIndex < str.length(); ++nIndex) {
            char ch = str.charAt(nIndex);
            if (ch < 0x80) {
                localResult += ch;
            }
        }
        return localResult;
    }

    public static boolean isNumber(String str) {
        for (int nIndex = 0; nIndex < str.length(); ++nIndex) {
            char ch = str.charAt(nIndex);
            if ((ch < '0') || (ch > '9')) {
                return false;
            }
        }
        return true;
    }

    public static boolean haveNumber(String str) {
        for (int nIndex = 0; nIndex < str.length(); ++nIndex) {
            char ch = str.charAt(nIndex);
            if ((ch >= '0') && (ch <= '9')) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWild(String str) {
        if (str.indexOf("*") != -1) {
            return true;
        }
        if (str.indexOf("?") != -1) {
            return true;
        }
        return false;
    }

    public static String buildDate(String str, ArrayList< SearchModelField> paramDateFieldList) {
//  输出格式 +字段名1:( ([low to high ]) or (+[low to high ]) ... ) or 
//  +字段名2:( (+[low to high ]) or (+[low to high ]) ... ) or      
        String localSrcStr = str;
        String localResultStr = "";
        int localDateFlag = 0;    //  1 岁 , 2 年 ,3月,4 日

        if (localSrcStr.endsWith("岁")) {
            localSrcStr = localSrcStr.substring(0, localSrcStr.length() - 1);
            localDateFlag = 1;
        } else if (localSrcStr.endsWith("年")) {
            localSrcStr = localSrcStr.substring(0, localSrcStr.length() - 1);
            localDateFlag = 2;
        } else if (localSrcStr.endsWith("月")) {
            localSrcStr = localSrcStr.substring(0, localSrcStr.length() - 1);
            localDateFlag = 3;
        } else if (localSrcStr.endsWith("日")) {
            localSrcStr = localSrcStr.substring(0, localSrcStr.length() - 1);
            localDateFlag = 4;
        } else {
            return "";
        }

        if (!haveNumber(localSrcStr)) {
            return "";
        }

        localSrcStr = clearChinese(localSrcStr);
//  分隔符优先级, ',','-'          
        String[] localStrList = localSrcStr.split(",");
        if (localStrList == null) {
            return str;
        }
        for (SearchModelField localDefine : paramDateFieldList) {
            String localFieldResultStr = "";  //  每个字段的条件
            for (String localStr : localStrList) {
//  对于每个条件,两者之间是或的关系
                String localColResultStr = "";
                String localRangeList[] = localStr.split("-");
                if (localRangeList == null) {
                    continue;
                }
                if (localRangeList.length > 2) {
                    continue;
                }
                if (!isNumber(localRangeList[0])) {
                    continue;
                }
                if ((localRangeList.length > 1) && (!isNumber(localRangeList[0]))) {
                    continue;
                }

                if (localRangeList.length == 1) {
                    // 单个日期     
                    localColResultStr = buildDateCond(localRangeList[0], localDefine);
                } else if (localRangeList.length == 2) {
                    if (((localRangeList[0].length() > 3) || ((localRangeList[1].length() > 3)))
                            && (localRangeList[0].length() != localRangeList[1].length())) {
                        continue;
                    }
                    localColResultStr = buildDateCond(localRangeList[0], localRangeList[1], localDefine);
                }
                if ((localColResultStr != null) && (!localColResultStr.isEmpty())) {
                    if (localFieldResultStr.isEmpty()) {
                        localFieldResultStr = " ( " + localColResultStr;
                    } else {
                        localFieldResultStr += " OR " + localColResultStr;

                    }
                }
            }
            if ((localFieldResultStr != null) && (!localFieldResultStr.isEmpty())) {
                localFieldResultStr += ")";
                if (localResultStr.isEmpty()) {
                    localResultStr = "( " + localFieldResultStr;
                } else {
                    localResultStr += " OR " + localFieldResultStr;

                }
            }

        }
        if ((localResultStr != null) && (!localResultStr.isEmpty())) {
            localResultStr += ")";
        }
        return localResultStr;
    }

    public static String buildDateCond(String paramDateStr,
            SearchModelField paramDateField) {
        String localResultStr = "";
        Calendar localDate = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        String localBeginDateStr;
        String localEndDateStr;
        int localYear = 0;
        int localMonth = 0;
        if (paramDateStr.length() < 4) {
//  年龄,字段名
            localYear = Integer.parseInt(paramDateStr);
            if (paramDateField.getFieldSpec() == 11) {
                localBeginDateStr = String.format("%04d0101", localDate.get(Calendar.YEAR) - localYear);
                localEndDateStr = String.format("%04d0101", localDate.get(Calendar.YEAR) - localYear + 1);
            } else if (paramDateField.getFieldSpec() == 12) {
                localBeginDateStr = String.format("%04d0101000000", localDate.get(Calendar.YEAR) - localYear);
                localEndDateStr = String.format("%04d0101000000", localDate.get(Calendar.YEAR) - localYear + 1);
            } else {
                return "";
            }
            localResultStr = " (+" + paramDateField.getSourceField() + ":[ " + localBeginDateStr
                    + " TO " + localEndDateStr + " ])";
        } else if (paramDateStr.length() == 6) {
//  给定月
            localYear = Integer.parseInt(paramDateStr.substring(0, 4));
            localMonth = Integer.parseInt(paramDateStr.substring(4, 6));
            if (paramDateField.getFieldSpec() == 11) {
                localBeginDateStr = String.format("%04d%02d01", localYear, localMonth);
                if (localMonth == 12) {
                    localEndDateStr = String.format("%04d%02d01", localYear + 1, 1);
                } else {
                    localEndDateStr = String.format("%04d%02d01", localYear, localMonth + 1);
                }
            } else if (paramDateField.getFieldSpec() == 12) {
                localBeginDateStr = String.format("%04d%02d01000000", localYear, localMonth);
                if (localMonth == 12) {
                    localEndDateStr = String.format("%04d%02d01000000", localYear + 1, 1);
                } else {
                    localEndDateStr = String.format("%04d%02d01000000", localYear, localMonth + 1);
                }
            } else {
                return "";
            }
            localResultStr = " (+" + paramDateField.getSourceField() + ":[ " + localBeginDateStr
                    + " TO " + localEndDateStr + " ])";

        } else if (paramDateStr.length() == 4) {
//  给定年
            localYear = Integer.parseInt(paramDateStr.substring(0, 4));
            if (paramDateField.getFieldSpec() == 11) {
                localBeginDateStr = String.format("%04d0101", localYear);
                localEndDateStr = String.format("%04d1231", localYear);
            } else if (paramDateField.getFieldSpec() == 12) {
                localBeginDateStr = String.format("%04d0101000000", localYear);
                localEndDateStr = String.format("%04d1231000000", localYear);
            } else {
                return "";
            }
            localResultStr = " (+" + paramDateField.getSourceField() + ":[ " + localBeginDateStr
                    + " TO " + localEndDateStr + " ])";

        } else if (paramDateStr.length() == 8) {
//  给定日期
            if (paramDateField.getFieldSpec() == 11) {
                localResultStr = " (+" + paramDateField.getSourceField() + ":" + paramDateStr + ")";
            } else if (paramDateField.getFieldSpec() == 12) {
                localBeginDateStr = paramDateStr + "000000";
                localEndDateStr = paramDateStr += "235959";
                localResultStr = " (+" + paramDateField.getSourceField() + ":[ " + localBeginDateStr
                        + " TO " + localEndDateStr + " ])";
            } else {
                return "";
            }
        } else if (paramDateStr.length() == 14) {
//  给定秒 
            if (paramDateField.getFieldSpec() == 12) {
                localResultStr = " (+" + paramDateField.getSourceField() + ":" + paramDateStr + ")";
            } else if (paramDateField.getFieldSpec() == 11) {
                localResultStr = " (+" + paramDateField.getSourceField() + ":" + paramDateStr.substring(0, 8) + ")";
            } else {
                return "";
            }

        } else {
            return "";
        }
        return localResultStr;
    }

    public static String buildDateCond(String paramBeginDateStr, String paramEndDateStr,
            SearchModelField paramDateField) {
        String localResultStr = "";
        Calendar localDate = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        String localBeginDateStr;
        String localEndDateStr;
        int localBeginYear = 0;
        int localBeginMonth = 0;
        int localEndYear = 0;
        int localEndMonth = 0;
        String localParam1;
        String localParam2;
        if (paramBeginDateStr.compareTo(paramEndDateStr) > 0) {
            localParam1 = paramEndDateStr;
            localParam2 = paramBeginDateStr;
        } else {
            localParam1 = paramBeginDateStr;
            localParam2 = paramEndDateStr;
        }
        if (localParam1.length() < 4) {
//  年龄,字段名
            localBeginYear = Integer.parseInt(localParam1);
            localEndYear = Integer.parseInt(localParam2);
            if (localBeginYear > localEndYear) {
                localBeginYear = Integer.parseInt(localParam2);
                localEndYear = Integer.parseInt(localParam1);
            }

            if (paramDateField.getFieldSpec() == 11) {
                localBeginDateStr = String.format("%04d0101", localDate.get(Calendar.YEAR) - localEndYear);
                localEndDateStr = String.format("%04d0101", localDate.get(Calendar.YEAR) - localBeginYear);
            } else if (paramDateField.getFieldSpec() == 12) {
                localBeginDateStr = String.format("%04d0101000000", localDate.get(Calendar.YEAR) - localEndYear);
                localEndDateStr = String.format("%04d0101000000", localDate.get(Calendar.YEAR) - localBeginYear + 1);
            } else {
                return "";
            }
            localResultStr = " (+" + paramDateField.getSourceField() + ":[ " + localBeginDateStr
                    + " TO " + localEndDateStr + " ])";
        } else if (localParam1.length() == 6) {
//  给定月
            localBeginYear = Integer.parseInt(localParam1.substring(0, 4));
            localBeginMonth = Integer.parseInt(localParam1.substring(4, 6));
            localEndYear = Integer.parseInt(localParam2.substring(0, 4));
            localEndMonth = Integer.parseInt(localParam2.substring(4, 6));
            if (paramDateField.getFieldSpec() == 11) {
                localBeginDateStr = String.format("%04d%02d01", localBeginYear, localBeginMonth);
                if (localEndMonth == 12) {
                    localEndDateStr = String.format("%04d%02d01", localEndYear + 1, 1);
                } else {
                    localEndDateStr = String.format("%04d%02d01", localEndYear, localEndMonth + 1);
                }
            } else if (paramDateField.getFieldSpec() == 12) {
                localBeginDateStr = String.format("%04d%02d01000000", localBeginYear, localBeginMonth);
                if (localEndMonth == 12) {
                    localEndDateStr = String.format("%04d%02d01000000", localEndYear + 1, 1);
                } else {
                    localEndDateStr = String.format("%04d%02d01000000", localEndYear, localEndMonth + 1);
                }
            } else {
                return "";
            }
            localResultStr = " (+" + paramDateField.getSourceField() + ":[ " + localBeginDateStr
                    + " TO " + localEndDateStr + " ])";

        } else if (localParam1.length() == 4) {
//  给定年
            localBeginYear = Integer.parseInt(localParam1.substring(0, 4));
            localEndYear = Integer.parseInt(localParam2.substring(0, 4));
            if (paramDateField.getFieldSpec() == 11) {
                localBeginDateStr = String.format("%04d0101", localBeginYear);
                localEndDateStr = String.format("%04d1231", localEndYear);
            } else if (paramDateField.getFieldSpec() == 12) {
                localBeginDateStr = String.format("%04d0101000000", localBeginYear);
                localEndDateStr = String.format("%04d1231000000", localEndYear);
            } else {
                return "";
            }
            localResultStr = " (+" + paramDateField.getSourceField() + ":[ " + localBeginDateStr
                    + " TO " + localEndDateStr + " ])";

        } else if (localParam1.length() == 8) {
//  给定日期
            if (paramDateField.getFieldSpec() == 11) {
                localResultStr = " (+" + paramDateField.getSourceField() + ":[" + localParam1
                        + " TO " + localParam2 + "])";
            } else if (paramDateField.getFieldSpec() == 12) {
                localBeginDateStr = localParam1 + "000000";
                localEndDateStr = localParam2 += "235959";
                localResultStr = " (+" + paramDateField.getSourceField() + ":[ " + localBeginDateStr
                        + " TO " + localEndDateStr + " ])";
            } else {
                return "";
            }
        } else if (localParam1.length() == 14) {
//  给定秒 
            if (paramDateField.getFieldSpec() == 12) {
                localResultStr = " (+" + paramDateField.getSourceField() + ":[" + localParam1
                        + " TO " + localParam2 + ")";
            } else if (paramDateField.getFieldSpec() == 11) {
                localResultStr = " (+" + paramDateField.getSourceField() + ":[" + localParam1.substring(0, 8)
                        + " TO " + localParam2.substring(0, 8) + ")";
            } else {
                return "";
            }

        } else {
            return "";
        }
        return localResultStr;
    }

    public static String keyPrefix = "+-\"\'";

    public static String keyWild = "*?";

    public static SearchFormat buildFormat(String paramQ, HashMap< String, SearchModelField> paramMap1,
            HashMap< String, SearchModelField> paramMap2, ArrayList< SearchModelField> paramWildFieldList,
            ArrayList< SearchModelField> paramDateFieldList) {

        SearchFormat localFormat = new SearchFormat();
        localFormat.searchQ = paramQ;

        SearchModelField modelField;
        String localDateResultStr = "";

        String[] localStrList = paramQ.split("[\\s\\x20]");
        if ((localStrList == null) || (localStrList.length <= 0)) {
            return localFormat;
        }

        //String localResultStr = "";
        StringBuilder searchQBuilder = new StringBuilder();
        //  拆分字符串成字符串数组        
        for (String localStr : localStrList) {
            //  对于每一个字符串进行处理        
            if ((localStr == null) || (localStr.isEmpty())) {
                continue;
            }
            //  包含关键字,则直接相加            
            if (keyWords.contains(localStr)) {
                searchQBuilder.append(" ").append(localStr).append(" ");
                continue;
            }
            localFormat.searchItemList.add(localStr);
        }
        //  搜索的字符串清单在searchItemList里        
        for (String localStr : localFormat.searchItemList) {
            if (localStr.contains(":") || localStr.contains("：")) {
                //  按字段搜索                
                String[] localKeyValue = localStr.split(":");
                if (localKeyValue.length < 2) {
                    localKeyValue = localStr.split("：");
                }
                if (localKeyValue.length < 2) {
                    if (searchQBuilder.length() > 0) {
                        searchQBuilder.append(" ");
                    }

                    searchQBuilder.append("+").append(localKeyValue[0]);

                    localFormat.searchKeyFieldSet.add(localKeyValue[0]);
                } else {
                    String localKeyName = "";

                    modelField = paramMap1.get(localKeyValue[0]);
                    if (modelField == null) {
                        modelField = paramMap2.get(localKeyValue[0]);

                    }
                    if (modelField != null) {
                        localKeyName = modelField.getSourceField();
                    }

                    if (searchQBuilder.length() > 0) {
                        searchQBuilder.append(" AND ");
                    }

                    if ((localKeyName != null) && (!localKeyName.isEmpty())) {
                        //  转换成 key_s:*value* OR key_tcn:value
                        int fieldSpec = modelField.getFieldSpec();
                        if ((checkFieldToken(modelField) || isWild(localKeyValue[1]))
                                && ((fieldSpec != 11) && (fieldSpec != 12) && (fieldSpec != 13))) {
                            int tmpIndex = 0;
                            searchQBuilder.append(" +(");

                            String tmpResultStr = "";
                            for (String tmpStr : modelField.getSourceFields()) {
                                if (tmpIndex > 0) {
                                    tmpResultStr += " OR ";
                                }
                                if ((tmpStr.endsWith("_s")) && (!isWild(localKeyValue[1]))) {
                                    tmpResultStr = tmpStr + ":*" + localKeyValue[1] + "* ";
                                    tmpIndex++;
                                    break;
                                } else if (tmpStr.endsWith("_tcn")) {
                                    tmpIndex++;
                                    tmpResultStr += tmpStr + ":" + localKeyValue[1];
                                } else {
                                    tmpIndex++;
                                    tmpResultStr += "+" + tmpStr + ":" + localKeyValue[1];
                                }
                            }
                            searchQBuilder.append(tmpResultStr).append(") ");

                        } else {
                            if ((fieldSpec == 11) || (fieldSpec == 12) || (fieldSpec == 13)) {
                                ArrayList<SearchModelField> localDateFieldList = new ArrayList<>();
                                localDateFieldList.add(modelField);
                                String localDateStr = buildDate(localKeyValue[1], localDateFieldList);
                                if ((localDateStr != null) && (!localDateStr.isEmpty())) {
                                    searchQBuilder.append(localDateStr);
                                } else {
                                    searchQBuilder.append("+").append(localKeyName).append(":").append(localKeyValue[1]);
                                }
                            } else {
                                searchQBuilder.append("+").append(localKeyName).append(":").append(localKeyValue[1]);
                            }
                        }
                        if (!isWild(localKeyValue[1])) {
                            localFormat.searchKeyFieldMap.put(localKeyValue[0], localKeyValue[1]);
                        }
                    } else {
                        searchQBuilder.append("+").append(localKeyValue[1]);
                        localFormat.searchKeyFieldSet.add(localKeyValue[1]);
                    }
                }

            } else if (isWild(localStr)) {
                //  通配的处理,在配置的通配字段里进行搜索,各字段是OR的关系                
                localStr = localStr.toUpperCase();
                if ("*".compareTo(localStr) == 0) {
                    if (searchQBuilder.length() > 0) {
                        searchQBuilder.append(" ");
                    }

                    searchQBuilder.append("*:*");
                    continue;
                }
                if ((paramWildFieldList == null) || (paramWildFieldList.isEmpty())) {
                    if (searchQBuilder.length() > 0) {
                        searchQBuilder.append(" ");
                    }
                    searchQBuilder.append("+").append(localStr);
                    continue;
                }

                StringBuilder matchStrBuilder = new StringBuilder();
                for (SearchModelField paramWildField : paramWildFieldList) {
                    if (matchStrBuilder.length()==0) {
                        matchStrBuilder.append(" +((+").append(paramWildField.getSourceField()).append(":").append(localStr);
                    } else {
                        matchStrBuilder.append(" OR (+").append(paramWildField.getSourceField()).append(":").append(localStr);
                    }
                    matchStrBuilder.append(" )");
                }
                matchStrBuilder.append(" ) ");

                if (searchQBuilder.length() > 0) {
                    searchQBuilder.append(" ");
                }
                searchQBuilder.append(matchStrBuilder.toString());

            } else {
                /*  查找样式
                 xx岁
                 xx-xxx
                 xxxx-xxxx
                 xxxxxx-xxxxxx
                 xxxxxxxx-xxxxxx
                 */
                localDateResultStr = buildDate(localStr, paramDateFieldList);
                if ((localDateResultStr != null) && (!localDateResultStr.isEmpty())) {
                    if (searchQBuilder.length() > 0) {
                        searchQBuilder.append(" AND ");
                    }
                    searchQBuilder.append("+").append(localDateResultStr);
                } else {
                    //  普通查找
                    localFormat.searchKeyFieldSet.add(localStr);
                    if (searchQBuilder.length() > 0) {
                        searchQBuilder.append(" AND ");
                    }
                    searchQBuilder.append("+\"").append(localStr).append("\"");
                }
            }
        }

        localFormat.searchQ = searchQBuilder.toString();
        System.out.println("format search query is :" + searchQBuilder.toString());

        return localFormat;
    }

    /**
     *
     * @param modelField
     * @return
     */
    public static boolean checkFieldToken(SearchModelField modelField) {
        //  判断是否有tcn的字段        
        boolean rc = false;

        String[] sourceFields = modelField.getSourceFields();
        if ((sourceFields == null) || (sourceFields.length < 1)) {
            return rc;
        }
        for (String tmpStr : sourceFields) {
            if (tmpStr.endsWith("_tcn") || tmpStr.endsWith("_s")) {
                rc = true;
                break;
            }
        }

        return rc;
    }
}
