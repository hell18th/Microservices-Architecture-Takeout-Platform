package com.sky.order.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务端
 */
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {
    /**
     * 存储连接会话，key为sid，value为Session对象
     */
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数，建立连接时的session
     * @param sid     用户唯一标识符
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        sessionMap.put(sid, session);
        System.out.println("连接 " + sid + " 已建立，当前在线人数：" + sessionMap.size());
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        sessionMap.remove(sid);
        System.out.println("连接 " + sid + " 已关闭，当前在线人数：" + sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送的消息
     * @param sid     用户唯一标识符
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("来自 " + sid + " 的消息：" + message);
    }

    /**
     * 广播消息给所有在线用户
     *
     * @param message 要广播的消息
     */
    public void broadcast(String message) {
        for (String sid : sessionMap.keySet()) {
            Session session = sessionMap.get(sid);
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}