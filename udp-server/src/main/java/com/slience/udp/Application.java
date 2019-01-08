/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slience.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.ResourceLeakDetector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Len
 */
public class Application {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "9000"));

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();

        try {
            // Configure the client.

            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);

            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator())
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new MyHandler());

            // Start the client.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.WARNING, null, ex);
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}
