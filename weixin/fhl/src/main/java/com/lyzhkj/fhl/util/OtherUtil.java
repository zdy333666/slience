/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.util;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.lyzhkj.fhl.conf.FHLConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.service.bean.Terminal;
//import com.service.cache.CacheManager;
//import com.service.db.DbManager;
//import com.service.other.Logger;
//import com.service.other.ServerConfig;

/**
 *
 * @author breeze
 */
public class OtherUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtherUtil.class);

    /**
     * 将返回码和返回信息拼接成json字符串
     *
     * @param code
     * @param info
     * @return json
     */
    public static String getMsg(String code, String info) {
        StringBuilder s = new StringBuilder();
        s.append("{\"code\" : \"" + code + "\",");
        s.append("\"info\" : \"" + info + "\"}");
        return s.toString();
    }

    /**
     * 检查指定发放机是否离线
     *
     * @param terminalNo
     * @return
     */
//	public static boolean checkTerminalIsOnline(String terminalNo) {
//		// 判断发放机是否离线
//		
//		if (null == terminalNo || "".equals(terminalNo.trim())) {
//			// 如果没有发放机编号, 不判断为离线
//			return true;
//		}
//		Terminal terminal = CacheManager.instance().terminalMap.get(terminalNo);
//		if (null == terminal || terminal.munit.equals("LYZH")) {
//			// 缓存里不存在该发放机编号, 或者测试用环境
//			return true;
//		}
//		
//		Date lastestTime = DbManager.instance().dbo.getTerminalLastestOnlineTime(
//			terminalNo);
//		
//		if (null == lastestTime ) {
//		
//			// 如果该发放机没有最后在线时间, 或者当前时间与最后在线时间差大于10分钟了, 判断为发放机离线
//			// 写入日志, 发送邮件
//			// 如果该发放机没有最后在线时间, 或者当前时间与最后在线时间差大于10分钟了, 判断为发放机离线, 如果不是测试环境, 发送邮件
//			CacheManager.instance().executorService.execute(
//				new SendMailThread("发放机已离线", "checkTerminalIsOnline, " + terminalNo + "发放机已离线"));
//		
//			Logger.debug(terminalNo, "Command:checkTerminalIsOnline, fail 发放机已离线, 请联系实施人员" );
//			return false;
//		}
//		return true;
//	}
    public static void iGtPush(String pushstr, String jpushregid, String terminalNo) {
        // 新建一个IGtPush实例，传入调用接口网址，appkey和masterSecret
        IGtPush push = new IGtPush(FHLConfig.IGTPUSH_HOST, FHLConfig.IGTPUSH_APPKEY, FHLConfig.IGTPUSH_MASTER);

        // 使用透传的消息类型
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(FHLConfig.IGTPUSH_APPID);
        template.setAppkey(FHLConfig.IGTPUSH_APPKEY);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(pushstr); // 推送的内容

        // 单推消息的消息体
        SingleMessage msg = new SingleMessage();
        msg.setData(template);
        msg.setOfflineExpireTime(1000 * 30); // 此推送30秒有效期

        // 单个用户
        Target target = new Target();
        target.setAppId(FHLConfig.IGTPUSH_APPID);
        target.setClientId(jpushregid); // 机器的pushid，从机器发过来的心跳中取得这个值

        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(msg, target);
        } catch (RequestException e) {
            LOGGER.error("", e);
            ret = push.pushMessageToSingle(msg, target, e.getRequestId());
        }
        if (ret != null) {
            LOGGER.info("iGtPush", "terminalNo:" + terminalNo + " pushid:" + jpushregid + " Response :" + ret.getResponse().toString());
        } else {
            LOGGER.info("iGtPush", "terminalNo:" + terminalNo + " pushid:" + jpushregid + " 服务器响应异常 ");
        }
    }
}
