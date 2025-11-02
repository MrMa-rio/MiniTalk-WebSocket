package com.marsn.minitalkwebsocket.adapter.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefixo para mensagens destinadas ao servidor (Controller)
        config.setApplicationDestinationPrefixes("/app");
        // Prefixo para o broker (mensagens enviadas do servidor para o cliente)
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint que o cliente usará para se conectar ao WebSocket
        registry.addEndpoint("/ws-chat").withSockJS();
    }
}