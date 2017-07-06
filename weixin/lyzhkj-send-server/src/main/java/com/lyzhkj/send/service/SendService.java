package com.lyzhkj.send.service;

import com.service.webservice.out.NoticeMessageResult;
import com.service.webservice.out.WebServiceServerNoticeMessageImpl;
import com.service.webservice.out.WebServiceServerNoticeMessageImplService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendService.class);

    /**
     * 调用远程接口，发送验证码
     * @param telNo
     * @param sms
     * @return 
     */
    public  NoticeMessageResult sendSMS(String telNo, String sms) {
        NoticeMessageResult result = null;
        try {
            WebServiceServerNoticeMessageImplService factory = new WebServiceServerNoticeMessageImplService();
            WebServiceServerNoticeMessageImpl wsService = factory.getWebServiceServerNoticeMessageImplPort();
            result = wsService.sendSMS(telNo, sms);
            LOGGER.info("sendSMS", "resultCode:" + result.getCode() + " resultInfo:" + result.getInfo() + " telNo:" + telNo + " sms:" + sms, "Auto");
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return result;
    }

    /**
     * 调用远程接口, 发送邮件
     *
     * @param receiver 邮件接收人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return
     */
    public  NoticeMessageResult sendMail(String receiver, String subject, String content) {
        NoticeMessageResult result = null;
        try {
            WebServiceServerNoticeMessageImplService factory = new WebServiceServerNoticeMessageImplService();
            WebServiceServerNoticeMessageImpl wsService = factory.getWebServiceServerNoticeMessageImplPort();
            result = wsService.sendMail(receiver, subject, content);
            LOGGER.info(receiver, "resultCode:" + result.getCode() + " resultInfo:" + result.getInfo() + " receiver:" + receiver + " subject:" + subject + " content:" + content, "Auto");
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return result;
    }

}
