/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.webservice.out;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 *
 * @author breeze
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SendSMS_QNAME = new QName("http://out.webservice.service.com/", "sendSMS");
    private final static QName _SendSMSResponse_QNAME = new QName("http://out.webservice.service.com/", "sendSMSResponse");
    private final static QName _SendMail_QNAME = new QName("http://out.webservice.service.com/", "sendMail");
    private final static QName _SendMailResponse_QNAME = new QName("http://out.webservice.service.com/", "sendMailResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: com.service.webservice.out
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendSMS }
     *
     */
    public SendSMS createSendSMS() {
        return new SendSMS();
    }

    /**
     * Create an instance of {@link SendSMSResponse }
     *
     */
    public SendSMSResponse createSendSMSResponse() {
        return new SendSMSResponse();
    }

    /**
     * Create an instance of {@link SendMail }
     *
     * @return
     */
    public SendMail createSendMail() {
        return new SendMail();
    }

    /**
     * Create an instance of {@link SendMailResponse }
     *
     * @return
     */
    public SendMailResponse createSendMailResponse() {
        return new SendMailResponse();
    }

    /**
     * Create an instance of {@link NoticeMessageResult }
     *
     */
    public NoticeMessageResult createNoticeMessageResult() {
        return new NoticeMessageResult();
    }

    /**
     * Create an instance of
     * {@link JAXBElement }{@code <}{@link SendSMS }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://out.webservice.service.com/", name = "sendSMS")
    public JAXBElement<SendSMS> createSendSMS(SendSMS value) {
        return new JAXBElement<SendSMS>(_SendSMS_QNAME, SendSMS.class, null, value);
    }

    /**
     * Create an instance of
     * {@link JAXBElement }{@code <}{@link SendSMSResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://out.webservice.service.com/", name = "sendSMSResponse")
    public JAXBElement<SendSMSResponse> createSendSMSResponse(SendSMSResponse value) {
        return new JAXBElement<SendSMSResponse>(_SendSMSResponse_QNAME, SendSMSResponse.class, null, value);
    }

    /**
     * Create an instance of
     * {@link JAXBElement }{@code <}{@link SendMail }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://out.webservice.service.com/", name = "sendMail")
    public JAXBElement<SendMail> createSendSMS(SendMail value) {
        return new JAXBElement<SendMail>(_SendMail_QNAME, SendMail.class, null, value);
    }

    /**
     * Create an instance of
     * {@link JAXBElement }{@code <}{@link SendMailResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://out.webservice.service.com/", name = "sendMailResponse")
    public JAXBElement<SendMailResponse> createSendMailResponse(SendMailResponse value) {
        return new JAXBElement<SendMailResponse>(_SendMailResponse_QNAME, SendMailResponse.class, null, value);
    }

}
