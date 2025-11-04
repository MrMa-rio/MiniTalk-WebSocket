package com.marsn.minitalkwebsocket.adapter.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final ProtoMessageConverterSocket messageConverterSocket;

    public WebSocketConfig(ProtoMessageConverterSocket messageConverterSocket) {
        this.messageConverterSocket = messageConverterSocket;
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefixo para mensagens destinadas ao servidor (Controller)
        config.setApplicationDestinationPrefixes("/app");

        // Prefixo para o broker (mensagens enviadas do servidor para o cliente)
        config.enableSimpleBroker("/topic");
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(messageConverterSocket);
        return false;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint que o cliente usará para se conectar ao WebSocket
        registry.addEndpoint("/ws-chat").withSockJS().setDisconnectDelay(10);
    }
}