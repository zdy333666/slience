/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.controller;

import cn.slience.springboot.demo.pojo.EventModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author breeze
 */
@Controller
public class EventSourceController {

    private static final Logger logger = LoggerFactory.getLogger(EventSourceController.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "event", method = RequestMethod.GET)
    public void event(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        PrintWriter out = response.getWriter();
        while (true) {
            out.write("id:" + UUID.randomUUID().toString() + "\n\n");
            //out.write("event:" +""+ "\n\n");
            out.write("data:" + objectMapper.writeValueAsString(new EventModel("hello", new Date())) + "\n\n");
            //out.write("retry:" + 5000 + "\n\n");
             out.flush();
             
             logger.info("send event...");
            TimeUnit.SECONDS.sleep(2);
        }
    }

}
