/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.see;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;

/**
 *
 * @author breeze
 */
public class  MovementServlet extends EventSourceServlet { 


    @Override
    protected EventSource newEventSource(HttpServletRequest hsr) {
        return new MovementEventSource(800, 600, 20); 
    }
}