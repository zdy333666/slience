/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
//@Component
public class SimpleCORSFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) {
        
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS,HEAD,GET,POST,PUT,DELETE");//,TRACE,PATCH
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin,Content-Type,Authorization,X-Requested-With,Accept"); //No-Cache,If-Modified-Since,Pragma,Last-Modified,Cache-Control,Expires,X-E4M-With,
   
        chain.doFilter(req, res);
    }
    
    @Override
    public void destroy() {
        
    }
    
}
