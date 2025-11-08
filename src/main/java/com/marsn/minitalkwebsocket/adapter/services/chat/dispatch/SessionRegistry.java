package com.marsn.minitalkwebsocket.adapter.services.chat.dispatch;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {

    private static class SessionData {
        WebSocketSession session;
        Sinks.Many<byte[]> sink;

        SessionData(WebSocketSession session, Sinks.Many<byte[]> sink) {
            this.session = session;
            this.sink = sink;
        }
    }

    private final Map<String, SessionData> sessions = new ConcurrentHashMap<>();

    public void registerSession(String userId, WebSocketSession session) {
        Sinks.Many<byte[]> sink = Sinks.many().unicast().onBackpressureBuffer();
        sessions.put(userId, new SessionData(session, sink));
    }

    public void unregisterSession(String userId) {
        var data = sessions.remove(userId);
        if (data != null) {
            data.sink.tryEmitComplete();
        }
    }

    public WebSocketSession getSession(String userId) {
        var data = sessions.get(userId);
        return data != null ? data.session : null;
    }

    public Sinks.Many<byte[]> getSink(String userId) {
        var data = sessions.get(userId);
        return data != null ? data.sink : null;
    }
}
