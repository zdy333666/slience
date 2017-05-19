/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.webservice.out;

import com.lyzhkj.fhl.conf.FHLConfig;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 *
 * @author breeze
 */
public class WebServiceServerNoticeMessageImplService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://out.webservice.service.com/", "WebServiceServer_NoticeMessageImplService");
    public final static QName WebServiceServerNoticeMessageImplPort = new QName("http://out.webservice.service.com/", "WebServiceServer_NoticeMessageImplPort");

    static {
        URL url = null;
        try {
            String ip = FHLConfig.NOTICEMESSAGE_WEBSERVICE_SERVER_IP;
            int port = FHLConfig.NOTICEMESSAGE_WEBSERVICE_SERVER_PORT;
            String URL = "http://" + ip + ":" + port + "/WebServiceServer_NoticeMessageImpl?wsdl";
            url = new URL(URL);
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(WebServiceServerNoticeMessageImplService.class.getName())
                    .log(java.util.logging.Level.INFO,
                            "Can not initialize the default wsdl from {0}", "http://192.168.10.63:8090/WebServiceServer_NoticeMessageImpl?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public WebServiceServerNoticeMessageImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public WebServiceServerNoticeMessageImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public WebServiceServerNoticeMessageImplService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public WebServiceServerNoticeMessageImplService(WebServiceFeature... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public WebServiceServerNoticeMessageImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public WebServiceServerNoticeMessageImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return returns WebServiceServerNoticeMessageImpl
     */
    @WebEndpoint(name = "WebServiceServer_NoticeMessageImplPort")
    public WebServiceServerNoticeMessageImpl getWebServiceServerNoticeMessageImplPort() {
        return super.getPort(WebServiceServerNoticeMessageImplPort, WebServiceServerNoticeMessageImpl.class);
    }

    /**
     *
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
     * configure on the proxy. Supported features not in the
     * <code>features</code> parameter will have their default values.
     * @return returns WebServiceServerNoticeMessageImpl
     */
    @WebEndpoint(name = "WebServiceServer_NoticeMessageImplPort")
    public WebServiceServerNoticeMessageImpl getWebServiceServerNoticeMessageImplPort(WebServiceFeature... features) {
        return super.getPort(WebServiceServerNoticeMessageImplPort, WebServiceServerNoticeMessageImpl.class, features);
    }

}
