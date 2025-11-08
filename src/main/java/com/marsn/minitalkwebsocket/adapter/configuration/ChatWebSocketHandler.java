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

    public ChatWebSocketHandler(SessionRegistry sessionRegistry, ProcessAdapter adapter, GenericConsumer genericConsumer) {
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

        return session.receive() //Recebe os dados do client e envia para o servidor de processamento
                .doOnNext(msg -> {
                    adapter.handleSendMessage(msg.getPayload());
                })
                .doFinally(signal -> sessionRegistry.unregisterSession(userId))
                .then();
    }


}
