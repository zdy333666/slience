/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzh.mqtt;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttVersion;
import io.netty.util.ReferenceCountUtil;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Len
 */
@ChannelHandler.Sharable
public class MyHandler extends ChannelInboundHandlerAdapter {

    private static final AtomicInteger counter = new AtomicInteger(0);

    private static final MqttFixedHeader heartBeatFixedHeader = new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
    private static final MqttMessage heartBeatMsg = new MqttMessage(heartBeatFixedHeader);
    private static final String prefix = "It's not who I am underneath, but what I do that defines me. --";  //Hello MQTT-

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();

        System.out.println("channelActive-->" + ctx);

        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader("MQTT", MqttVersion.MQTT_3_1_1.protocolLevel(), false, false, false, 0, false, false, 300);
        MqttConnectPayload payload = new MqttConnectPayload("test-" + System.currentTimeMillis(), "topic", new byte[0], null, new byte[0]);
        MqttConnectMessage msg = new MqttConnectMessage(mqttFixedHeader, variableHeader, payload);

        ctx.writeAndFlush(msg);

        //发送心跳信息-------------------------------------
        ctx.channel().eventLoop().scheduleWithFixedDelay(() -> {
            ctx.writeAndFlush(heartBeatMsg);
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            MqttMessage mqttMsg = (MqttMessage) msg;
            System.out.println("messageType-->" + mqttMsg.fixedHeader().messageType().name());
//            System.out.println("payload-->" + mqttMsg.payload());

            if (mqttMsg.fixedHeader().messageType() == MqttMessageType.PINGRESP) {
                
                String words = prefix + counter.incrementAndGet();
                byte[] data = words.getBytes();
//            ByteBuf payload = ctx.alloc()
//                    .directBuffer(data.length)
//                    .writeBytes(data);

//                Unpooled.directBuffer(data.length).writeBytes(data);

                MqttPublishMessage publishMsg = MqttMessageBuilders.publish()
                        .qos(MqttQoS.AT_LEAST_ONCE)
                        .retained(false)
                        .topicName("topic")
                        .messageId(counter.get())
                        .payload(Unpooled.wrappedBuffer(data))
                        .build();

                ctx.writeAndFlush(publishMsg);
            }

        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered-->" + ctx);
        ctx.fireChannelUnregistered();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }

}
