/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.conf;

/**
 *
 * @author zdy
 */
public class RelationConfiguration {

    public static final String RELATION_TYPE = "Person->Hotel";
    public static final String RELATION_SAVEPOINT_TABLE = "relation_savepoint";

    public static final String GAZHK_LGY_NB = "gazhk_LGY_NB";
    
    public static final String NEO4J_URI = "jdbc:neo4j://10.118.128.182:7474/";

    /*
     slaveOk=true,false
     OR
     readPreference=primary,primaryPreferred,secondary,secondaryPreferred,nearest
     */
    public static final String MONGO_URI_RTDB = "mongodb://hzga:hzga5678@10.118.128.177:30013,10.118.128.178:30013,10.118.128.179:30013,10.118.128.181:30013,10.118.128.182:30013/rtdb?readPreference=primary";
    public static final String MONGO_URI_HZGA = "mongodb://hzga:hzga5678@10.118.128.177:30012,10.118.128.178:30012,10.118.128.179:30012,10.118.128.181:30012,10.118.128.182:30012/hzga?readPreference=primary";

}
