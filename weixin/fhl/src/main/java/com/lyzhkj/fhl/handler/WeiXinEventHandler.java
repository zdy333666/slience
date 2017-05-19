/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.handler;

import java.util.Map;

/**
 *
 * @author breeze
 */
public interface WeiXinEventHandler {

    public void handle(Map<String, String> body);

}
