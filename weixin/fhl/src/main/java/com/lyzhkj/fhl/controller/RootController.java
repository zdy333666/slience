/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.conf.FHLConfig;
import com.lyzhkj.fhl.conf.WeiXinMenuConfig;
import static com.lyzhkj.fhl.conf.WeiXinMenuConfig.WEIXIN_MENU_JSON;
import static com.lyzhkj.fhl.conf.WeiXinMenuConfig.initMenu;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinMenuUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author breeze
 */
@Controller
public class RootController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootController.class);

    /**
     *用于系统正常状态测试
     * @return
     */
    @RequestMapping("/")
    @ResponseBody
    public String getPage() {

        String result = "test is ok";
        System.out.println(result);

        return result;
    }

    /**
     *用于创建微信公众号菜单
     * @return
     */
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    @ResponseBody
    public String createMenu(@RequestParam(required = false) String domain) {

        String result = "weixin menu creation failure";

        if (!(domain == null || domain.trim().isEmpty())) {
            FHLConfig.APP_URL = new StringBuilder("http://").append(domain).toString();

            WeiXinMenuConfig.WEIXIN_MENU_JSON = JSONObject.fromObject(initMenu()).toString();
            LOGGER.debug("WEIXIN_MENU_JSON-->" + WEIXIN_MENU_JSON);
        }

        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();

        LOGGER.info("weixin-menu-content-->" + WeiXinMenuConfig.WEIXIN_MENU_JSON);

        int code = WeiXinMenuUtil.createMenu(token.getAccessToken(), WeiXinMenuConfig.WEIXIN_MENU_JSON);
        if (code == 0) {
            result = "weixin menu creation successful";
        }

        LOGGER.info(result);

        return result;
    }

}
