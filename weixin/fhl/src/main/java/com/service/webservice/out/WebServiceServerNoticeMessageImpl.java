/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.webservice.out;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 *
 * @author breeze
 */
@WebService(targetNamespace = "http://out.webservice.service.com/", name = "WebServiceServer_NoticeMessageImpl")
@XmlSeeAlso({ObjectFactory.class})
public interface WebServiceServerNoticeMessageImpl {

    @WebMethod
    @RequestWrapper(localName = "sendSMS", targetNamespace = "http://out.webservice.service.com/", className = "com.service.webservice.out.SendSMS")
    @ResponseWrapper(localName = "sendSMSResponse", targetNamespace = "http://out.webservice.service.com/", className = "com.service.webservice.out.SendSMSResponse")
    @WebResult(name = "return", targetNamespace = "")
    public NoticeMessageResult sendSMS(
            @WebParam(name = "arg0", targetNamespace = "") java.lang.String arg0,
            @WebParam(name = "arg1", targetNamespace = "") java.lang.String arg1
    );

    @WebMethod
    @RequestWrapper(localName = "noticeMessage", targetNamespace = "http://out.webservice.service.com/", className = "com.service.webservice.out.NoticeMessage")
    @ResponseWrapper(localName = "noticeMessageResponse", targetNamespace = "http://out.webservice.service.com/", className = "com.service.webservice.out.NoticeMessageResponse")
    @WebResult(name = "return", targetNamespace = "")
    public NoticeMessageResult noticeMessage(
            @WebParam(name = "arg0", targetNamespace = "") int arg0,
            @WebParam(name = "arg1", targetNamespace = "") java.lang.String arg1
    );

    @WebMethod
    @RequestWrapper(localName = "sendMail", targetNamespace = "http://out.webservice.service.com/", className = "com.service.webservice.out.SendMail")
    @ResponseWrapper(localName = "sendMailResponse", targetNamespace = "http://out.webservice.service.com/", className = "com.service.webservice.out.SendMailResponse")
    @WebResult(name = "return", targetNamespace = "")
    public NoticeMessageResult sendMail(
            @WebParam(name = "arg0", targetNamespace = "") java.lang.String arg0,
            @WebParam(name = "arg1", targetNamespace = "") java.lang.String arg1,
            @WebParam(name = "arg2", targetNamespace = "") java.lang.String arg2
    );

}
