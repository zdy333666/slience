/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.ws;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@ServerEndpoint(value = "/websocket")
@Component
public class MyWebSocket {

//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicLong onlineCount = new AtomicLong(0L);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1

        StringBuilder str = new StringBuilder("one new connection opened,current online number is ").append(getOnlineCount());
        System.out.println(str);
//        try {
//            sendMessage(str.toString());
//        } catch (IOException e) {
//            System.out.println("IO异常");
//        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("one connection closed,current online number is " + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("msg come from client:" + message);

        Gson gson = new GsonBuilder().create();

        //群发消息
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(gson.toJson(new Greeting("Hello, " + message + "!")));
            } catch (IOException ex) {
                Logger.getLogger(MyWebSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @OnError
     */
    public void onError(Session session, Throwable error) {
        System.out.println("error occured");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     *
     */
    public static void sendInfo(String message) throws IOException {
        for (MyWebSocket item : webSocketSet) {
            item.sendMessage(message);
        }
    }

    public static long getOnlineCount() {
        return onlineCount.get();
    }

    public static void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        onlineCount.decrementAndGet();
    }
}
