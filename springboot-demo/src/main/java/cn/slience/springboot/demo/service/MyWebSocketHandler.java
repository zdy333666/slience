/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 *
 * @author breeze
 */
public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        session.setBinaryMessageSizeLimit(8192);
        session.setTextMessageSizeLimit(8192);

        logger.info("session ConnectionEstablished :{}", session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("session ConnectionClosed :{}", session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {

        logger.info("receive message:{} from session:{}", message.getPayload(), session);

        Map<String, Object> reply = new LinkedHashMap<>();
        reply.put("sessonId", session.getId());
        reply.put("uri", session.getUri());
        reply.put("data", "hello to see you");
        reply.put("time", new Date());

        try {
            String replyStr = new ObjectMapper().writeValueAsString(reply);
            session.sendMessage(new TextMessage(replyStr));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }

    }

}
