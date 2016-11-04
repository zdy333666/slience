/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

/**
 *
 * @author xzh
 */
public class SearchInput {
    private String busiModel;   //  必须提供，业务模型，指出是搜索那种业务应用，通常就是指哪个core。
    private String groupName; // 模型组名字 搜索模型组下所包含的业务模型 busiModel和groupName必须包含一个
    private int gridView;   //  是否是grid视图，如果是1，那么则返回的字段不是完整的字段。缺省为0，表示发送完整的字段
    private String viewName;    //  视图名字，缺省使用内部的视图名，通常就是业务模型的名称。
    private String q;   //  必须提供，搜索的关键字
    private String rawq;    //  原始字符串
    private int start;  //  缺省为0，返回的结果从偏移量多少开始。
    private int rows;   //  缺省为10，本次查询返回多少条记录。
    private String sort;    //  可以没有，按哪个字段排序.
    private String oldq;    //  可选使用，表示是上一次查询的关键字。
    private String stats;   //统计关键字
    private String limit;
    
    /**
     * @return the busiModel
     */
    public String getBusiModel() {
        return busiModel;
    }

    /**
     * @param busiModel the busiModel to set
     */
    public void setBusiModel(String busiModel) {
        this.busiModel = busiModel;
    }

    /**
     * @return the gridView
     */
    public int getGridView() {
        return gridView;
    }

    /**
     * @param gridView the gridView to set
     */
    public void setGridView(int gridView) {
        this.gridView = gridView;
    }

    /**
     * @return the viewName
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * @param viewName the viewName to set
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * @return the q
     */
    public String getQ() {
        return q;
    }

    /**
     * @param q the q to set
     */
    public void setQ(String q) {
        this.q = q;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return the sort
     */
    public String getSort() {
        return sort;
    }

    /**
     * @param sort the sort to set
     */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * @return the rawq
     */
    public String getRawq() {
        return rawq;
    }

    /**
     * @param rawq the rawq to set
     */
    public void setRawq(String rawq) {
        this.rawq = rawq;
    }

    /**
     * @return the oldq
     */
    public String getOldq() {
        return oldq;
    }

    /**
     * @param oldq the oldq to set
     */
    public void setOldq(String oldq) {
        this.oldq = oldq;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the stats
     */
    public String getStats() {
        return stats;
    }

    /**
     * @param stats the stats to set
     */
    public void setStats(String stats) {
        this.stats = stats;
    }

    /**
     * @return the limit
     */
    public String getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(String limit) {
        this.limit = limit;
    }
    
}
