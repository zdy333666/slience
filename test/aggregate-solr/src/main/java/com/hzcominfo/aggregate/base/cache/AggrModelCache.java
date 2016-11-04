/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.cache;

import com.hzcominfo.aggregate.base.pojo.AggrModel;
import com.hzcominfo.aggregate.base.pojo.AggrModelField;
import com.hzcominfo.aggregate.base.pojo.AggrSetModelParam;
import java.util.List;

/**
 *
 * @author cominfo4
 */
public class AggrModelCache {

    private AggrModel model;
    private List<AggrSetModelParam> params;
    private List<AggrModelField> fields;

    /**
     * @return the model
     */
    public AggrModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(AggrModel model) {
        this.model = model;
    }

    /**
     * @return the params
     */
    public List<AggrSetModelParam> getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(List<AggrSetModelParam> params) {
        this.params = params;
    }

    /**
     * @return the fields
     */
    public List<AggrModelField> getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(List<AggrModelField> fields) {
        this.fields = fields;
    }

}
