/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import net.sf.json.JSONObject;

/**
 *
 * @author breeze
 */
public class WeiXinQrUtil {

    /**
     * 从微信服务器获得二维码ticket
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String getQrTicket(String accessToken, String scene_str) {
        String ticket = null;
        try {
            //{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_id": 123}}}

            JSONObject sceneNode = new JSONObject();
            sceneNode.put("scene_str", scene_str);

            JSONObject action_infoNode = new JSONObject();
            action_infoNode.put("scene", sceneNode);

            JSONObject param = new JSONObject();
            param.put("action_name", "QR_LIMIT_STR_SCENE");
            param.put("action_info", action_infoNode);

            String url = WeiXinConfig.CREATE_QR_URL.replace("ACCESS_TOKEN", accessToken);
            JSONObject jsonObject = HttpUtil.doPostStr(url, param.toString());
            if (jsonObject != null && jsonObject.containsKey("ticket")) {
                ticket = jsonObject.getString("ticket");
                //jsonObject.getString("ticket");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }
    
    public static String getQrURL(String ticket) {
        String url = WeiXinConfig.GET_QR_IMAGE.replace("TICKET", ticket);
        return url;
    }

}
