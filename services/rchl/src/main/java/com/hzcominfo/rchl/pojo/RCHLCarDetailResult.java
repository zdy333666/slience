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
public class RCHLCarDetailResult implements RCHLDetailResult {

    private List<RCHLCarDetail> mongoCLHCList;
    private Page page;

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

    /**
     * @return the mongoCLHCList
     */
    public List<RCHLCarDetail> getMongoCLHCList() {
        return mongoCLHCList;
    }

    /**
     * @param mongoCLHCList the mongoCLHCList to set
     */
    public void setMongoCLHCList(List<RCHLCarDetail> mongoCLHCList) {
        this.mongoCLHCList = mongoCLHCList;
    }

}
