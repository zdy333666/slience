/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.conf;

/**
 *
 * @author zdy
 */
public class AggregateConfiguration {

    public static final String AGGREGATE_ATTRSET = "query_attrset_define";
    public static final String AGGREGATE_ATTR = "query_attr_define";
    public static final String AGGREGATE_ATTRSET_PARAM = "query_attrset_param";
    public static final String AGGREGATE_DIMENSION = "query_dimension_define";
    public static final String AGGREGATE_DIMENSION_ATTRSET = "query_dimension_attrset";
    //public static final String AGGREGATE_QGRK_CODE = "query_qgrk_code_map";
    //public static final String AGGREGATE_QGRK_BACKUP = "query_qgrk_backup";
    public static final String AGGREGATE_LOG = "log_query_aggr_service";

    public static final String MONGO_URI_RTDB = "mongodb://hzga:hzga5678@10.118.128.177:30013,10.118.128.178:30013,10.118.128.179:30013,10.118.128.181:30013,10.118.128.182:30013/rtdb";
    public static final String MONGO_URI_HZGA = "mongodb://hzga:hzga5678@10.118.128.177:30012,10.118.128.178:30012,10.118.128.179:30012,10.118.128.181:30012,10.118.128.182:30012/hzga?readPreference=secondaryPreferred";
   
    /*
     slaveOk=true,false
     OR
     readPreference=primary,primaryPreferred,secondary,secondaryPreferred,nearest
     */
}
