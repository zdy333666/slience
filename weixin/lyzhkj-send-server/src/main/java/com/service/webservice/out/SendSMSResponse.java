/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.webservice.out;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author breeze
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendSMSResponse", propOrder = {
    "_return"
})
public class SendSMSResponse {

    @XmlElement(name = "return")
    protected NoticeMessageResult _return;

    /**
     * ��ȡreturn���Ե�ֵ��
     *
     * @return possible object is {@link NoticeMessageResult }
     *
     */
    public NoticeMessageResult getReturn() {
        return _return;
    }

    /**
     * ����return���Ե�ֵ��
     *
     * @param value allowed object is {@link NoticeMessageResult }
     *
     */
    public void setReturn(NoticeMessageResult value) {
        this._return = value;
    }

}
