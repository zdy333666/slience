/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl;

import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;

/**
 *
 * @author breeze
 */
public class QRLinkTest {
 
    public static void main(String[] args){
        
        //生成URL
	StringBuilder builder = new StringBuilder("http://dd2h6s.natappfree.cc/exchangeMachinePay?");
        builder.append("terminalNo=").append("000000300084"); //机器编号  
        builder.append("&goodsId=").append("LYZHSHOPPR00042"); //商品编号
        builder.append("&goodsName=").append("舒肤佳 香皂 薰衣草舒缓呵护115g*3"); //商品名称
        builder.append("&score=").append(20);  //所需积分
        builder.append("&price=").append(5); //价格，单位：元
	
	String url =WeiXinUserUtil.getCodeRequest(builder.toString());
        
        System.out.println(url);
    }
    
    
}
