/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.conf;

import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WeiXinMenu;
import com.lyzhkj.weixin.common.pojo.WeiXinMenuButton;
import com.lyzhkj.weixin.common.pojo.WeiXinMenuClickButton;
import com.lyzhkj.weixin.common.pojo.WeiXinMenuViewButton;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@Component
public class WeiXinMenuConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinMenuConfig.class);

    public static String WEIXIN_MENU_JSON;

    static {
        WEIXIN_MENU_JSON = JSONObject.fromObject(initMenu()).toString();
        LOGGER.debug("WEIXIN_MENU_JSON-->" + WEIXIN_MENU_JSON);
    }

    /**
     * 初始化Menu
     *
     * @return
     */
    public static WeiXinMenu initMenu() {
        WeiXinMenu menu = new WeiXinMenu();
        menu.setButton(new WeiXinMenuButton[]{initGroupOne(), initGroupTwo(), initGroupThree()});
        return menu;
    }

    /*
	 * 知识|活动
     */
    public static WeiXinMenuButton initGroupOne() {
        WeiXinMenuButton button = new WeiXinMenuButton("知识|活动");

        WeiXinMenuButton sub_button_1 = new WeiXinMenuViewButton("分类知识库", "view", FHLConfig.APP_URL + "/category-repository");// WeiXinUserUtil.getCodeRequest(FHLConfig.APP_URL + "/category-repository"));
        WeiXinMenuButton sub_button_2 = new WeiXinMenuViewButton("分类政策", "view", WeiXinUserUtil.getCodeRequest(FHLConfig.APP_URL + "/category-policy"));//WeiXinUserUtil.getCodeRequest(FHLConfig.APP_URL + "/category-policy"));
        WeiXinMenuButton sub_button_3 = new WeiXinMenuViewButton("积分礼遇", "view", WeiXinUserUtil.getCodeRequest(FHLConfig.APP_URL + "/integral"));
        button.setSub_button(new WeiXinMenuButton[]{sub_button_1, sub_button_2, sub_button_3});

        return button;
    }

    /*
	 * 服务|个人
     */
    public static WeiXinMenuButton initGroupTwo() {
        WeiXinMenuButton button = new WeiXinMenuButton("服务|个人");

        WeiXinMenuButton sub_button_1 = new WeiXinMenuViewButton("举报反馈", "view", WeiXinUserUtil.getCodeRequest(FHLConfig.APP_URL + "/report-feedback"));
        //WeiXinMenuButton sub_button_2 = new WeiXinMenuViewButton("我的分类", "view", WeiXinUserUtil.getCodeRequest(FHLConfig.APP_URL + "/category-detail"));
        WeiXinMenuButton sub_button_2 = new WeiXinMenuClickButton("我的分类", "click", "104");

        WeiXinMenuButton sub_button_3 = new WeiXinMenuClickButton("扫码领袋", "scancode_push", "1022");
        //WeiXinMenuButton sub_button_3 = new WeiXinMenuClickButton("扫码领袋", "click", "1022");
        WeiXinMenuButton sub_button_4 = new WeiXinMenuViewButton("个人中心", "view", WeiXinUserUtil.getCodeRequest(FHLConfig.APP_URL + "/person-center"));
        //WeiXinMenuButton sub_button_5 = new WeiXinMenuClickButton("扫码共享", "click", "105");

        button.setSub_button(new WeiXinMenuButton[]{sub_button_1, sub_button_2, sub_button_3, sub_button_4});//, sub_button_5});

        return button;
    }

    /*
	 * 服务|商户
     */
    public static WeiXinMenuButton initGroupThree() {
        WeiXinMenuButton button = new WeiXinMenuButton("服务|商户");

        WeiXinMenuButton sub_button_1 = new WeiXinMenuViewButton("扫码收款", "view", WeiXinUserUtil.getCodeRequest(FHLConfig.APP_URL + "/pay"));
        WeiXinMenuButton sub_button_2 = new WeiXinMenuClickButton("兑换查询", "click", "203");

        button.setSub_button(new WeiXinMenuButton[]{sub_button_1, sub_button_2});

        return button;
    }

//    public static void main(String[] args){
//          try {
//            //         FileConfiguration config = new FileConfiguration(WeiXinMenuLoader.class.getResourceAsStream("config/weixin-menu.json"));
//
//            InputStream stream = WeiXinMenuLoader.class.getResourceAsStream("/config/weixin-menu.json");
//            WEIXIN_MENU_JSON = new ObjectMapper().readTree(stream).toString();
//            stream.close();
//
//        } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(WeiXinMenuLoader.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        System.out.println("WEIXIN_MENU_JSON:"+WEIXIN_MENU_JSON);
//    }
}
