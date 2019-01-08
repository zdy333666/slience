/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzh.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttVersion;
import io.netty.util.ReferenceCountUtil;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Len
 */
@ChannelHandler.Sharable
public class MyHandler extends ChannelInboundHandlerAdapter {

    private static final MqttFixedHeader heartBeatFixedHeader = new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
    private static final MqttMessage heartBeatMsg = new MqttMessage(heartBeatFixedHeader);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();

        System.out.println("channelActive-->" + ctx);

        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader("MQTT", MqttVersion.MQTT_3_1_1.protocolLevel(), false, false, false, 0, false, false, 10);
        MqttConnectPayload payload = new MqttConnectPayload("test-" + System.currentTimeMillis(), "topic", new byte[0], null, new byte[0]);
        MqttConnectMessage msg = new MqttConnectMessage(mqttFixedHeader, variableHeader, payload);

        ctx.writeAndFlush(msg);

        //发送心跳信息-------------------------------------
        ctx.channel().eventLoop().scheduleWithFixedDelay(() -> {
            ctx.writeAndFlush(heartBeatMsg);
        }, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            MqttMessage mqttMsg = (MqttMessage) msg;
            MqttMessageType messageType = mqttMsg.fixedHeader().messageType();

            System.out.println("messageType-->" + messageType);

            switch (messageType) {
                case CONNACK:
                    MqttSubscribeMessage subscribeMsg = MqttMessageBuilders.subscribe()
                            .addSubscription(MqttQoS.EXACTLY_ONCE, "topic")
                            .messageId(1)
                            .build();
                    ctx.writeAndFlush(subscribeMsg);
                    break;
                case SUBACK:
                    MqttSubAckMessage subAckMsg = (MqttSubAckMessage) mqttMsg;
                    System.out.println("subAckMsg payload-->" + subAckMsg.payload().grantedQoSLevels());
                    break;
                case PUBLISH:
                    MqttPublishMessage pubMsg = (MqttPublishMessage) mqttMsg;
                    ByteBuf payload = (ByteBuf) pubMsg.payload();
                    System.out.println("payload-->" + payload.getCharSequence(0, payload.readableBytes(), StandardCharsets.UTF_8).toString());

                    int packetId = pubMsg.variableHeader().packetId();
//                     System.out.println("packetId-->" + packetId);

                    //回复PUBREC
                    MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.AT_MOST_ONCE, false, 2);
                    MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(packetId);
                    MqttPubAckMessage pubAckMsg = new MqttPubAckMessage(mqttFixedHeader, variableHeader);

                    ctx.writeAndFlush(pubAckMsg);
                    break;
                case PUBREL:
                    //回复PUBCOMP
                    MqttFixedHeader mqttPubCOMPFixedHeader = new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_LEAST_ONCE, false, 2);
                    MqttMessage pubCOMPMsg = new MqttMessage(mqttPubCOMPFixedHeader, mqttMsg.variableHeader());

                    ctx.writeAndFlush(pubCOMPMsg);
                    break;
                default:
                    break;
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
