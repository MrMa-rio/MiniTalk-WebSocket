package com.marsn.minitalkwebsocket.adapter.services.chat.dispatch;

import com.marsn.minitalkwebsocket.adapter.services.consumers.GenericConsumer;
import com.marsn.minitalkwebsocket.core.model.rabbit.Exchanges;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessChatQueueKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessExchangeKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessRoutingKey;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {

    private final Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();



    public void registerSession(String userId, WebSocketSession session) {
        activeSessions.put(userId, session);
        System.out.printf("🔗 Sessão registrada: %s (%s)%n", userId, session.getId());
    }

    public void unregisterSession(String userId) {
        activeSessions.remove(userId);
        System.out.printf("❌ Sessão removida: %s%n", userId);
    }

    public WebSocketSession getSession(String userId) {
        return activeSessions.get(userId);
    }


}
