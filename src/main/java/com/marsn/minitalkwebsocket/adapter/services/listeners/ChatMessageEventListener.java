package com.marsn.minitalkwebsocket.adapter.services.listeners;

import com.marsn.minitalkwebsocket.adapter.services.chat.dispatch.ConnectionService;
import com.marsn.minitalkwebsocket.adapter.services.events.ChatMessageEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageEventListener {

    private final ConnectionService connectionService;

    public ChatMessageEventListener(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @EventListener
    public void onChatMessage(ChatMessageEvent event) {
        connectionService.deliverMessageToUser(event.getUserId(), event.getProtobufMessage());
    }
}
