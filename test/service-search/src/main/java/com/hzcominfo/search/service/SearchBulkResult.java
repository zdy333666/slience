/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

import java.util.ArrayList;

/**
 *
 * @author xzh
 */
public class SearchBulkResult {
    private ArrayList < BulkResultItem> bulkResultItem;

    /**
     * @return the bulkResultItem
     */
    public ArrayList < BulkResultItem> getBulkResultItem() {
        return bulkResultItem;
    }

    /**
     * @param bulkResultItem the bulkResultItem to set
     */
    public void setBulkResultItem(ArrayList < BulkResultItem> bulkResultItem) {
        this.bulkResultItem = bulkResultItem;
    }
    
}
