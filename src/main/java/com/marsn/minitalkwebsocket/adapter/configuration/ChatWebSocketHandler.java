package com.marsn.minitalkwebsocket.adapter.configuration;

import com.marsn.minitalkwebsocket.adapter.services.chat.dispatch.SessionRegistry;
import com.marsn.minitalkwebsocket.adapter.services.chat.process.ProcessAdapter;
import com.marsn.minitalkwebsocket.adapter.services.consumers.GenericConsumer;
import com.marsn.minitalkwebsocket.core.model.rabbit.Exchanges;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessChatQueueKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessExchangeKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessRoutingKey;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    private final SessionRegistry sessionRegistry;
    private final ProcessAdapter adapter;
    private final GenericConsumer genericConsumer;

    public ChatWebSocketHandler(SessionRegistry sessionRegistry,
                                ProcessAdapter adapter,
                                GenericConsumer genericConsumer) {
        this.sessionRegistry = sessionRegistry;
        this.adapter = adapter;
        this.genericConsumer = genericConsumer;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String userId = session.getHandshakeInfo().getUri().getQuery()
                .replace("userId=", "");

        sessionRegistry.registerSession(userId, session);

        var processChatQueueKey = new ProcessChatQueueKey(userId);
        var processExchangeKey = new ProcessExchangeKey(Exchanges.DELIVERY_EXCHANGE);
        var processRoutingKey = new ProcessRoutingKey(userId);

        genericConsumer.subscribeToDynamicQueue(
                processChatQueueKey,
                processExchangeKey,
                processRoutingKey
        );

        var sink = sessionRegistry.getSink(userId);
        if (sink == null) {
            sessionRegistry.registerSession(userId, session);
        }

        var outgoing = sessionRegistry.getSink(userId)
                .asFlux()
                .map(bytes -> session.binaryMessage(factory -> factory.wrap(bytes)));

        var incoming = session.receive()
                .doOnNext(msg -> adapter.handleSendMessage(msg.getPayload()))
                .then();

        return Mono.when(
                session.send(outgoing),
                incoming
        ).doFinally(signal -> {
            sessionRegistry.unregisterSession(userId);
        });
    }

}
