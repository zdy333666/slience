/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.archive.conf;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 *
 * @author cominfo4
 */
public class MongoSource {

    private MongoClient client;
    private MongoClientURI clientURI;

    /**
     * @return the client
     */
    public MongoClient getClient() {
        return client;
    }

    /**
     * @param aClient the client to set
     */
    public void setClient(MongoClient aClient) {
        client = aClient;
    }

    /**
     * @return the clientURI
     */
    public MongoClientURI getClientURI() {
        return clientURI;
    }

    /**
     * @param clientURI the clientURI to set
     */
    public void setClientURI(MongoClientURI clientURI) {
        this.clientURI = clientURI;
    }

}
