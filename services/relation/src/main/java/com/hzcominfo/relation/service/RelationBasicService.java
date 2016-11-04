/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.service;

import com.hzcominfo.relation.conf.MongoSource;
import com.hzcominfo.relation.conf.MongoSourceFactory;
import com.hzcominfo.relation.conf.RelationConfiguration;
import com.hzcominfo.relation.dao.RelationBasicDao;
import com.hzcominfo.relation.pojo.RelationDefine;
import com.mongodb.DBCollection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cominfo4
 */
@Service
public class RelationBasicService {

    @Autowired
    private RelationBasicDao relationBasicDao;

    /**
     * 获取关系定义信息
     */
    public List<RelationDefine> getRelationTypes() {

        MongoSource source = MongoSourceFactory.getSource(RelationConfiguration.mongo_uri_rtdb);
        DBCollection coll = source.getClient().getDB(source.getClientURI().getDatabase()).getCollection(RelationConfiguration.relation_define_table);

        return relationBasicDao.getRelationDefines(coll);
    }

}
