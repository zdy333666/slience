/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dto;

import com.lyzhkj.fhl.pojo.CategoryArticleIntro;
import java.util.List;

/**
 *
 * @author breeze
 */
public class PolicyPage {
    
    private int count;
    private List<CategoryArticleIntro> rows;

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the rows
     */
    public List<CategoryArticleIntro> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<CategoryArticleIntro> rows) {
        this.rows = rows;
    }
}
