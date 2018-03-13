/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.reactor.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.client.HttpClient;
import reactor.ipc.netty.http.client.HttpClientResponse;

/**
 *
 * @author breeze
 */
public class SimpleTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTest.class);

    public static void main(String[] args) {

        HttpClient client = HttpClient.create();
        Mono<HttpClientResponse> mono = client.get("http://www.baidu.com");
        //NOTE reactor.ipc.netty.http.client.MonoHttpClientResponse
        logger.info("mono resp:{}", mono);
        mono.subscribe();
        
    }

}
