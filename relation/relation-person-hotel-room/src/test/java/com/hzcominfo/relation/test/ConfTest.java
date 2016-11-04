package com.hzcominfo.relation.test;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.hzcominfo.relation.conf.RelationConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class ConfTest {

    @Scheduled(fixedRate = 3000)
    public void print() {

        System.out.println("---------------------->"+RelationConfiguration.mongo_uri_hzga);
        System.out.println("---------------------->"+RelationConfiguration.neo4j_bolt_uri);
        System.out.println();
        
    }

}
