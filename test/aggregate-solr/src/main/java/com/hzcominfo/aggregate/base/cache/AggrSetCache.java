/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.cache;

import com.hzcominfo.aggregate.base.pojo.AggrSet;
import java.util.Map;

/**
 *
 * @author cominfo4
 */
public class AggrSetCache {

    private AggrSet aggrSet;
    private Map<Long, AggrModelCache> aggrModelCaches;

    /**
     * @return the aggrSet
     */
    public AggrSet getAggrSet() {
        return aggrSet;
    }

    /**
     * @param aggrSet the aggrSet to set
     */
    public void setAggrSet(AggrSet aggrSet) {
        this.aggrSet = aggrSet;
    }

    /**
     * @return the aggrModelCaches
     */
    public Map<Long, AggrModelCache> getAggrModelCaches() {
        return aggrModelCaches;
    }

    /**
     * @param aggrModelCaches the aggrModelCaches to set
     */
    public void setAggrModelCaches(Map<Long, AggrModelCache> aggrModelCaches) {
        this.aggrModelCaches = aggrModelCaches;
    }

}
