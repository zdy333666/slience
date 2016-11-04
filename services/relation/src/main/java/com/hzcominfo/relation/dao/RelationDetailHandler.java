/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.pojo.RelationDetail;
import java.util.Collection;
import java.util.List;
import org.neo4j.driver.v1.Session;

/**
 *
 * @author cominfo4
 */
public interface  RelationDetailHandler {
    
    /**
     * 
     * @param session
     * @param pair
     * @param type
     * @return 
     */
    public Collection<RelationDetail> getDetail(Session session,String[] pair,String type);
    
}
