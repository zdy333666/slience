/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.handler;

import com.lyzhkj.fhl.service.ExchangeViewService;
import com.lyzhkj.fhl.service.MyCategoryService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@Component
public class WeiXinClickEventHandler implements WeiXinEventHandler {

    @Autowired
    private MyCategoryService myCategoryService;

    @Autowired
    private ExchangeViewService exchangeViewService;

    @Override
    public void handle(Map<String, String> body) {

        String eventKey = body.get("EventKey");
        String openId = body.get("FromUserName");

        //我的分类
        if ("104".equals(eventKey)) {
            //以上处理需要绑定后才能使用

            myCategoryService.service(openId);
        } else //兑换查询
        if ("203".equals(eventKey)) {
            //以上处理需要绑定后才能使用

            exchangeViewService.service(openId);
        }

    }

}
