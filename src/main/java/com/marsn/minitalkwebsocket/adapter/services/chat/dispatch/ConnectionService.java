package com.marsn.minitalkwebsocket.adapter.services.chat.dispatch;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class ConnectionService {

    private final SessionRegistry sessionRegistry;

    public ConnectionService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * Envia mensagem reativamente para o usuário (caso conectado).
     */
    public void deliverMessageToUser(String userId, byte[] protobufMessage) {
        Sinks.Many<byte[]> sink = sessionRegistry.getSink(userId);

        if (sink == null) {
            System.out.println("⚠️ Usuário " + userId + " não possui sessão ativa.");
            return;
        }

        Sinks.EmitResult result = sink.tryEmitNext(protobufMessage);

        if (result.isFailure()) {
            System.out.println("❌ Falha ao enviar mensagem para " + userId + ": " + result);
        }
    }
}
