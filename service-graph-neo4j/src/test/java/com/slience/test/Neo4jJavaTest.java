/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slience.test;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

/**
 *
 * @author slience
 */
public class Neo4jJavaTest {
    
    public static void main(String[] args){

        Driver driver = GraphDatabase.driver("bolt://localhost"); // <password>
        try (Session session = driver.session()) {
        
            session.close();
    }
        driver.close();
    
    }
    
}
