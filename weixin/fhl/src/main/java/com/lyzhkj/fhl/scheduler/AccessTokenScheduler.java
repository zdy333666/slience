/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.scheduler;

import com.lyzhkj.fhl.cache.Cache;
import com.lyzhkj.fhl.conf.WeiXinConfig;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@Component
public class AccessTokenScheduler {

    /**
     * 从微信服务器获得令牌(有效时间2H)
     *
     */
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 1)
    public void getAccessToken() {

        AccessToken accessToken =WeiXinAccessTokenUtil.getAccessToken(WeiXinConfig.ACCESS_TOKEN_URL, WeiXinConfig.APP_ID, WeiXinConfig.APP_SECRET);
        if (accessToken != null) {
            Cache.accessToken=accessToken;
        }

    }

}
