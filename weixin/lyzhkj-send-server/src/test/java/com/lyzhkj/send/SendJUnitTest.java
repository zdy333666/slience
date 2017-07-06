package com.lyzhkj.send;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author breeze
 */
public class SendJUnitTest {

    public SendJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    //@Test
    public void sendSMS() {

        Map<String, String> param = new HashMap<>();
        param.put("telNo", "13567179365");
        param.put("sms", "你好，我在测试短信发送功能");

        Map<String, Object> result = new RestTemplate().postForObject("http://localhost:6060/send/sms", param, Map.class);
        System.out.println("result-->" + result);

    }

    //@Test
    public void sendMail() {

        Map<String, String> param = new HashMap<>();
        param.put("receiver", "");
        param.put("subject", "测试");
        param.put("content", "你好");

        Map<String, Object> result = new RestTemplate().postForObject("http://localhost:6060/send/mail", param, Map.class);
        System.out.println("result-->" + result);

    }
}
