/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.conf;

/**
 *
 * @author zdy
 */
public class AggregateConfiguration {

    public static final String AGGREGATE_DIMENSION = "aggr_dimension";
    public static final String AGGREGATE_SET = "aggr_set";
    public static final String AGGREGATE_SET_MODEL_PARAM = "aggr_set_model_param";
    public static final String AGGREGATE_MODEL = "aggr_model";

    public static final String AGGREGATE_MODEL_FIELD = "aggr_model_field";

    public static final String AGGREGATE_QGRK_CODE = "query_qgrk_code_map";
    public static final String AGGREGATE_QGRK_BACKUP = "query_qgrk_backup";

    //public static final String AGGREGATE_LOG = "log_aggr_service";

    public static final String SEARCH_MODEL = "search_model_define";
    public static final String SEARCH_MODEL_FIELD = "search_model_field_define";
    
    public static final String MONGO_URI_RTDB = "mongodb://hzga:hzga5678@10.118.128.177:30013,10.118.128.178:30013,10.118.128.179:30013,10.118.128.181:30013,10.118.128.182:30013/rtdb";
    //public static final String MONGO_URI_HZGA = "mongodb://hzga:hzga5678@10.118.128.177:30012,10.118.128.178:30012,10.118.128.179:30012,10.118.128.181:30012,10.118.128.182:30012/hzga?readPreference=secondaryPreferred";
    //public static final String MONGO_URI_KAKOU = "mongodb://hzga:workhzga1234@10.118.128.177:50012,10.118.128.178:50012,10.118.128.179:50012,10.118.128.181:50012,10.118.128.182:50012/hzga";

    /*
     slaveOk=true,false
     OR
     readPreference=primary,primaryPreferred,secondary,secondaryPreferred,nearest
     */
}
