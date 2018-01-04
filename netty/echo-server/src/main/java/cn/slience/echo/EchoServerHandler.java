/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author breeze
 */
public class EchoServerHandler extends SimpleChannelInboundHandler {

    private final Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);

    int counter = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext chc, Object msg) throws Exception {

        String body = (String) msg;
        //System.out.println
        logger.info("This is " + (++counter) + " times receive client:[" + body + "]");
        body += "$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());

        chc.writeAndFlush(echo);
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
