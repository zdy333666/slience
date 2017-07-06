package com.lyzhkj.fhl;

import com.lyzhkj.fhl.conf.WeiXinMenuConfig;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinMenuUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author breeze
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableAsync
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);

        //createWeiXinMenu();      
    }

    public static void createWeiXinMenu() {
        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
        //
        LOGGER.info("WEIXIN_MENU_JSON-->" + WeiXinMenuConfig.WEIXIN_MENU_JSON);

        int result = WeiXinMenuUtil.createMenu(token.getAccessToken(), WeiXinMenuConfig.WEIXIN_MENU_JSON);
        if (result == 0) {
            LOGGER.info("创建Menu成功");
        } else {
            LOGGER.info("创建Menu失败");
            System.exit(-1);
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

}
