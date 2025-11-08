package com.marsn.minitalkwebsocket.adapter.services.chat.dispatch;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

@Service
public class ConnectionService {

    private final SessionRegistry sessionRegistry;

    public ConnectionService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * Método chamado quando o RabbitMQ envia uma mensagem para entrega.
     */
    public void deliverMessageToUser(String userId, byte[] protobufMessage) {
        WebSocketSession session = sessionRegistry.getSession(userId);

        if (session == null) {
            System.out.println("⚠️ Usuário não conectado a esta instância: " + userId);
            return;
        }

        if (!session.isOpen()) {
            System.out.println("❌ Sessão fechada para: " + userId);
            sessionRegistry.unregisterSession(userId);
            return;
        }

        // Cria mensagem binária
        WebSocketMessage binaryMessage =
                session.binaryMessage(factory -> factory.wrap(ByteBuffer.wrap(protobufMessage)));

        // Envia de forma reativa
        session.send(Mono.just(binaryMessage)).subscribe();
    }
}
