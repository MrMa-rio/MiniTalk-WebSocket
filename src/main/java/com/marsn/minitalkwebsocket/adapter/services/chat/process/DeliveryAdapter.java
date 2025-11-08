package com.marsn.minitalkwebsocket.adapter.services.chat.process;

import com.marsn.minitalkwebsocket.adapter.services.events.ChatMessageEvent;
import com.marsn.minitalkwebsocket.v1.ChatMessage;
import org.springframework.amqp.core.Message;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class DeliveryAdapter {

    private final ApplicationEventPublisher eventPublisher;

    public DeliveryAdapter(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void handleMessageReceived(ChatMessage chatMsg) {
        eventPublisher.publishEvent(new ChatMessageEvent(this, chatMsg.getDestinyId(), chatMsg.toByteArray()));
    }

    public void handleMessageError(Message message) {

    }
}
