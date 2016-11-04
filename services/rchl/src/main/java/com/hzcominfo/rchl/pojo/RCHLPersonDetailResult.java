/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.pojo;

import java.util.List;

/**
 *
 * @author cominfo4
 */
public class RCHLPersonDetailResult implements RCHLDetailResult {

    private List<RCHLPersonDetail> mongoRYHCList;
    private Page page;

    /**
     * @return the mongoRYHCList
     */
    public List<RCHLPersonDetail> getMongoRYHCList() {
        return mongoRYHCList;
    }

    /**
     * @param mongoRYHCList the mongoRYHCList to set
     */
    public void setMongoRYHCList(List<RCHLPersonDetail> mongoRYHCList) {
        this.mongoRYHCList = mongoRYHCList;
    }

    /**
     * @return the page
     */
    public Page getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(Page page) {
        this.page = page;
    }

}
