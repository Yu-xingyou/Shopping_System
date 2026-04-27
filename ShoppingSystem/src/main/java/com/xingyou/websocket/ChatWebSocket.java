package com.xingyou.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/ws/chat/{userId}")
public class ChatWebSocket {

    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        onlineUsers.put(userId, session);
        log.info("用户上线: {}, 当前在线人数: {}", userId, onlineUsers.size());

        try {
            sendJsonMessage(session, Map.of(
                    "type", "connected",
                    "message", "连接成功"
            ));
        } catch (IOException e) {
            log.error("发送连接消息失败", e);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") String userId) {
        log.debug("收到用户 {} 的消息: {}", userId, message);
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        onlineUsers.remove(userId);
        log.info("用户下线: {}, 当前在线人数: {}", userId, onlineUsers.size());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket错误, userId: {}",
                  session.getPathParameters().get("userId"), error);
    }

    public static void sendMessageToUser(String userId, Object data) throws IOException {
        Session session = onlineUsers.get(userId);
        if (session != null && session.isOpen()) {
            sendJsonMessage(session, data);
        }
    }

    public static boolean isOnline(String userId) {
        return onlineUsers.containsKey(userId);
    }

    private static void sendJsonMessage(Session session, Object data) throws IOException {
        session.getBasicRemote().sendText(objectMapper.writeValueAsString(data));
    }
}

