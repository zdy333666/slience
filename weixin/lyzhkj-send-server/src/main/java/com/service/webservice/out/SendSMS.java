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
@XmlType(name = "sendSMS", propOrder = {
    "arg0",
    "arg1"
})
public class SendSMS {

    protected String arg0;
    protected String arg1;

    /**
     * ��ȡarg0���Ե�ֵ��
     *
     * @return possible object is {@link String }
     *
     */
    public String getArg0() {
        return arg0;
    }

    /**
     * ����arg0���Ե�ֵ��
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setArg0(String value) {
        this.arg0 = value;
    }

    /**
     * ��ȡarg1���Ե�ֵ��
     *
     * @return possible object is {@link String }
     *
     */
    public String getArg1() {
        return arg1;
    }

    /**
     * ����arg1���Ե�ֵ��
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setArg1(String value) {
        this.arg1 = value;
    }

}
