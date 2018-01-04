/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.echo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author breeze
 */
public class EchoClientHandler extends SimpleChannelInboundHandler {
    
    private final Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);
    
    private int counter;
    static final String ECHO_REQ = "Hi, slience. Welcome to Netty.$_";
    
    public EchoClientHandler() {
        
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        
        logger.info("client:{}", "channelActive");
        
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
        
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext chc, Object msg) throws Exception {
        
        System.out.println("This is " + (++counter) + " times receive server:[" + msg + "]");
        
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        
        logger.error(cause.getMessage(), cause);
        
        ctx.close();
    }
    
}
