/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.webservice.out;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author breeze
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "noticeMessageResult", propOrder = {
    "code",
    "info"
})
public class NoticeMessageResult {

    protected int code;
    protected String info;

    public int getCode() {
        return code;
    }

    public void setCode(int value) {
        this.code = value;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String value) {
        this.info = value;
    }

}
