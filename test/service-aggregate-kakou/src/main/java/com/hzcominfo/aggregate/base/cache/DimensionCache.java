/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.cache;

import java.util.Map;

/**
 *
 * @author cominfo4
 */
public class DimensionCache {

    private String dimension;
    private Map<Long,AttrSetCache> attrSetCaches;

    /**
     * @return the dimension
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * @param dimension the dimension to set
     */
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    /**
     * @return the attrSetCaches
     */
    public Map<Long,AttrSetCache> getAttrSetCaches() {
        return attrSetCaches;
    }

    /**
     * @param attrSetCaches the attrSetCaches to set
     */
    public void setAttrSetCaches(Map<Long,AttrSetCache> attrSetCaches) {
        this.attrSetCaches = attrSetCaches;
    }


}
