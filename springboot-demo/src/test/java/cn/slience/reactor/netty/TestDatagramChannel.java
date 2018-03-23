/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.reactor.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author breeze
 */
public class TestDatagramChannel {

    public static void main(String[] args) {

        //receive();
        send();

    }

    public static void receive() {
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(8888));
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                ByteBuffer buf = ByteBuffer.allocate(1024);
                buf.clear();
                channel.receive(buf);

                buf.flip();
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void send() {
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                String info = "I'm the Sender At Time " + System.currentTimeMillis();
                ByteBuffer buf = ByteBuffer.allocate(1024);
                buf.clear();
                buf.put(info.getBytes());
                buf.flip();

                int bytesSent = channel.send(buf, new InetSocketAddress("127.0.0.1", 8888));
                System.out.println(bytesSent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
