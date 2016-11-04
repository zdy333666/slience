/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.authentication.filter;

import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author slience
 */
public class SimpleFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpSession session = request.getSession();
        //session.setMaxInactiveInterval(10);
        String username = "username";
        if (session.isNew()) {
            Map<String,Object> user =new HashMap<>();
            session.setAttribute("user", user);
            
            System.out.println("--------------------------------- session is new :" + session.getId());
        } else{
            System.out.println("--------------------------------- user is :" + (Map<String,Object>)session.getAttribute("user"));
        }
        
        System.out.println("--------------------------------- session timeout :" + session.getMaxInactiveInterval());
        
        //session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, username);

        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        return null;
    }

}
