/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.handler;

import com.lyzhkj.fhl.service.ScanCodeAndGetGarbageBagService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@Component
public class WeiXinScanCodePushEventHandler implements WeiXinEventHandler {

    @Autowired
    private ScanCodeAndGetGarbageBagService ScanCodeAndGetGarbageBagService;

    @Override
    public void handle(Map<String, String> body) {

        //String eventKey = body.get("EventKey");
        String openId = body.get("FromUserName");
        //扫码领袋
        ///if ("1022".equals(eventKey)) {

        String scanResult = body.get("EventKey");// body.get("ScanResult");

        //处理逻辑
        ScanCodeAndGetGarbageBagService.service(openId, scanResult);

        //}
    }

}
