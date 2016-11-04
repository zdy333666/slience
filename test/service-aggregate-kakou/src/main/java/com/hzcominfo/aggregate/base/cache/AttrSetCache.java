/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.cache;

import com.hzcominfo.aggregate.base.pojo.Attr;
import com.hzcominfo.aggregate.base.pojo.AttrSet;
import com.hzcominfo.aggregate.base.pojo.AttrSetParam;
import java.util.List;

/**
 *
 * @author cominfo4
 */
public class AttrSetCache {

    //private long attrSetId;
    //private String attrSetName;
    private AttrSet attrSet;
    private List<AttrSetParam> attrSetParams;
    private List<Attr> attrs;

    /**
     * @return the attrSet
     */
    public AttrSet getAttrSet() {
        return attrSet;
    }

    /**
     * @param attrSet the attrSet to set
     */
    public void setAttrSet(AttrSet attrSet) {
        this.attrSet = attrSet;
    }

    /**
     * @return the attrs
     */
    public List<Attr> getAttrs() {
        return attrs;
    }

    /**
     * @param attrs the attrs to set
     */
    public void setAttrs(List<Attr> attrs) {
        this.attrs = attrs;
    }

    /**
     * @return the attrSetParams
     */
    public List<AttrSetParam> getAttrSetParams() {
        return attrSetParams;
    }

    /**
     * @param attrSetParams the attrSetParams to set
     */
    public void setAttrSetParams(List<AttrSetParam> attrSetParams) {
        this.attrSetParams = attrSetParams;
    }

}
