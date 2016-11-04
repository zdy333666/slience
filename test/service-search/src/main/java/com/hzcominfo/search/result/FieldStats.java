/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.result;

import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author xzh
 */
public class FieldStats {
    public String fieldName;
    public TreeMap < String,Long> fieldStatsDataMap;
    public List <ResultStats > fieldStatsDataList;
}
