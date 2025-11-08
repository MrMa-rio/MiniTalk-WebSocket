package com.marsn.minitalkwebsocket.adapter.services.chat.process;

import com.marsn.minitalkwebsocket.adapter.services.chat.dispatch.ConnectionService;
import com.marsn.minitalkwebsocket.v1.ChatMessage;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

@Service
public class DeliveryAdapter {

    private final ConnectionService connectionService;

    public DeliveryAdapter(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public void handleMessageReceived(ChatMessage chatMsg) {
        connectionService.deliverMessageToUser(chatMsg.getDestinyId(), chatMsg.toByteArray());
    }

    public void handleMessageError(Message message) {

    }
}
