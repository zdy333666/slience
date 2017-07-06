/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.pojo;

import java.util.Date;

/**
 *
 * @author breeze
 */
public class GarProduct {

    private String id;
    private String name;
    private double price;
    private int unitprice;
    private String pic;
    private String description;
    private Date ExpiredDate;
    private String ExchangeCode;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the unitprice
     */
    public int getUnitprice() {
        return unitprice;
    }

    /**
     * @param unitprice the unitprice to set
     */
    public void setUnitprice(int unitprice) {
        this.unitprice = unitprice;
    }

    /**
     * @return the pic
     */
    public String getPic() {
        return pic;
    }

    /**
     * @param pic the pic to set
     */
    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the ExpiredDate
     */
    public Date getExpiredDate() {
        return ExpiredDate;
    }

    /**
     * @param ExpiredDate the ExpiredDate to set
     */
    public void setExpiredDate(Date ExpiredDate) {
        this.ExpiredDate = ExpiredDate;
    }

    /**
     * @return the ExchangeCode
     */
    public String getExchangeCode() {
        return ExchangeCode;
    }

    /**
     * @param ExchangeCode the ExchangeCode to set
     */
    public void setExchangeCode(String ExchangeCode) {
        this.ExchangeCode = ExchangeCode;
    }
}
