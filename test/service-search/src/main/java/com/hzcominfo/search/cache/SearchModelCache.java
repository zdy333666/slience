/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.cache;

import com.hzcominfo.search.pojo.SearchModel;
import com.hzcominfo.search.pojo.SearchModelField;
import java.util.List;

/**
 *
 * @author cominfo4
 */
public class SearchModelCache {
    
    private SearchModel model;
    private List<SearchModelField> modelFields;

    /**
     * @return the model
     */
    public SearchModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(SearchModel model) {
        this.model = model;
    }

    /**
     * @return the modelFields
     */
    public List<SearchModelField> getModelFields() {
        return modelFields;
    }

    /**
     * @param modelFields the modelFields to set
     */
    public void setModelFields(List<SearchModelField> modelFields) {
        this.modelFields = modelFields;
    }
}
