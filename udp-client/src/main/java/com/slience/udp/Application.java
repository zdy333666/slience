/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slience.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.ResourceLeakDetector;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Len
 */
public class Application {

    private static final String ECHO_STR = "hello, I am a UDP packet from client at time ";

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();

        try {
            // Configure the client.
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);

            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new CLientHandler());

            // Start the client.
            Channel ch = b.bind(0).sync().channel();

            ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(ECHO_STR + new Date(), CharsetUtil.UTF_8), new InetSocketAddress("127.0.0.1", 9000))).sync();

            // Wait until the connection is closed.
            ch.closeFuture().sync();

        } catch (Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, null, ex);
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

    private static class CLientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
            String body = packet.content().toString(CharsetUtil.UTF_8);
            System.out.println("packet-->" + body);

            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(ECHO_STR + new Date(), CharsetUtil.UTF_8), new InetSocketAddress("127.0.0.1", 9000))).sync();
        }
    }
}
