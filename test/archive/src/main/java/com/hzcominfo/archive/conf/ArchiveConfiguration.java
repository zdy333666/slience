/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.archive.conf;

/**
 *
 * @author zdy
 */
public class ArchiveConfiguration {

    public static final String AGGREGATE_ATTRSET = "query_attrset_define";
    public static final String AGGREGATE_ATTR = "query_attr_define";
    public static final String AGGREGATE_ATTRSET_PARAM = "query_attrset_param";
    //public static final String AGGREGATE_ATTR_EXTEND_PARAM = "query_attr_extend_param";
    public static final String AGGREGATE_DIMENSION = "query_dimension_define";
    public static final String AGGREGATE_DIMENSION_ATTRSET = "query_dimension_attrset";

    public static final String AGGREGATE_QGRK_CODE = "query_qgrk_code_map";
    public static final String AGGREGATE_QGRK_BACKUP = "query_qgrk_backup";

    public static final String AGGREGATE_LOG = "log_query_aggr_service";

    //public static final String SEARCH_MODEL_DEFINE = "search_model_define";
    //public static final String SEARCH_MODEL_FIELD_DEFINE = "search_model_field_define";
    public static final String MONGO_URI_RTDB = "mongodb://hzga:hzga5678@10.118.128.177:30013,10.118.128.178:30013,10.118.128.179:30013,10.118.128.181:30013,10.118.128.182:30013/rtdb";

}
