/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dto;

/**
 *
 * @author breeze
 */
public class ExchangeMachinePayParam {

    private String openId;
    private String terminalNo;
    private String goodsId;
    private int score;
    private long tradeTime;

    /**
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @return the terminalNo
     */
    public String getTerminalNo() {
        return terminalNo;
    }

    /**
     * @param terminalNo the terminalNo to set
     */
    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    /**
     * @return the goodsId
     */
    public String getGoodsId() {
        return goodsId;
    }

    /**
     * @param goodsId the goodsId to set
     */
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the tradeTime
     */
    public long getTradeTime() {
        return tradeTime;
    }

    /**
     * @param tradeTime the tradeTime to set
     */
    public void setTradeTime(long tradeTime) {
        this.tradeTime = tradeTime;
    }

}
