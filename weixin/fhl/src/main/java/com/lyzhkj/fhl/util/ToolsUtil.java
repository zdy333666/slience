package com.lyzhkj.fhl.util;

import com.service.webservice.out.NoticeMessageResult;
import com.service.webservice.out.WebServiceServerNoticeMessageImpl;
import com.service.webservice.out.WebServiceServerNoticeMessageImplService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ToolsUtil {
    
private static final Logger LOGGER = LoggerFactory.getLogger(ToolsUtil.class);

	//生成4位短信验证码
	public static String createCaptcha(){		
		return String.valueOf(Math.random()).substring(2, 6);
	}
	
	//调用远程接口，发送验证码
	public static NoticeMessageResult sendSMS(String telNo,String sms){
		NoticeMessageResult result = null;
		try {
			WebServiceServerNoticeMessageImplService factory=new WebServiceServerNoticeMessageImplService();	
			WebServiceServerNoticeMessageImpl wsService= factory.getWebServiceServerNoticeMessageImplPort();
			result = wsService.sendSMS(telNo, sms);					
			LOGGER.info("sendSMS", "resultCode:"+result.getCode()+" resultInfo:"+result.getInfo()+" telNo:"+telNo+" sms:"+sms, "Auto");
		} catch (Exception e) {
			LOGGER.error("",e);
		}
		return result;
	}

	/**
	 * 调用远程接口, 发送邮件
	 * @param receiver 邮件接收人
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @return
	 */
	public static NoticeMessageResult sendMail(String receiver, String subject, String content) {
		NoticeMessageResult result = null;
		try {
			WebServiceServerNoticeMessageImplService factory=new WebServiceServerNoticeMessageImplService();	
			WebServiceServerNoticeMessageImpl wsService= factory.getWebServiceServerNoticeMessageImplPort();
			result = wsService.sendMail(receiver, subject, content);
			LOGGER.info(receiver, "resultCode:" + result.getCode() + " resultInfo:" + result.getInfo() + " receiver:" + receiver + " subject:" + subject + " content:" + content, "Auto");
		} catch (Exception e) {
			LOGGER.error("",e);
		}
		return result;
	}
	
	//居民姓名模糊化
	public static String NameSecret(String moName){
		if (moName == null){
			return moName;
		}		
		if (moName.length() <= 1){
			return moName;
		}		
		return moName.substring(0,1) + "*" + moName.substring(2,moName.length());		
	}
	
	/**
	 * 根据类型 获得 垃圾袋类型
	 */
	public static String getGarTypeName(String garType){
		if("05".equals(garType)){
			return "(厨余垃圾)";
		}else{
			return "(其他垃圾)";
		}
	}
	
	/**
	 * 根据类型 获得 垃圾袋类型
	 */
	public static String getBagTypeName(String garType){
		if("01".equals(garType)){
			return "(厨余垃圾)";
		}else{
			return "(其他垃圾)";
		}
	}
	
	/**
	 * 根据类型 获得 垃圾袋类型
	 */
	public static String getBagPickType(String bagType){
		if("01".equals(bagType)){
			return "05";
		}else{
			return "06";
		}
	}
	
//	//消息输出 Xml
//	public static void OutPrint(Packet packet, String content){		
//		ResidentScalar ResidentScalar = CacheManager.instance().scalarCalers.get(packet.openID);		
//		ResidentScalar.out.print(MessageUtil.initTextMessage(packet, content));	
//		ResidentScalar.out.close();
//		Logger.err(MessageUtil.initTextMessage(packet, content));
//	}
//	
//	//图片输出 Xml
//	public static void OutPrintImage(Packet packet, String content){		
//		ResidentScalar ResidentScalar = CacheManager.instance().scalarCalers.get(packet.openID);		
//		ResidentScalar.out.print(MessageUtil.initImageMessage(packet, content));	
//		ResidentScalar.out.close();
//		Logger.err(MessageUtil.initImageMessage(packet, content));
//	}
	
	//消息输出 Json
//	public static void ReplyTextMessage(Packet packet, String content){				
//		ReplyTextMessage(packet,content,true); 
//	}
//	
//	public static void ReplyTextMessage(Packet packet, String content,boolean printerrlog){				
//		String message = MessageUtil.initReplyTextMessage(packet, content);
//		System.out.println(message);
//		WeixinUtil.replyMessage(CacheManager.instance().token.getToken(),message,printerrlog);
//	}
//	
//	//图片输出 Json
//	public static void ReplyImageMessage(Packet packet, String content){				
//		String message = MessageUtil.initReplyImageMessage(packet, content);
//		WeixinUtil.replyMessage(CacheManager.instance().token.getToken(),message);
//	}
//	//图文输出json
//	public static void ReplyByTeletext(Packet packet,List<Articles> articles) throws Exception{
//		String message = MessageUtil.initReplyTeletext(packet,articles);
//		WeixinUtil.replyMessage(CacheManager.instance().token.getToken(), message);
//	}
	
}
