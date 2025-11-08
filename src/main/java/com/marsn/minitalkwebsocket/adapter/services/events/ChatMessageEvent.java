package com.marsn.minitalkwebsocket.adapter.services.events;

import org.springframework.context.ApplicationEvent;

public class ChatMessageEvent extends ApplicationEvent {

    private final String userId;
    private final byte[] protobufMessage;

    public ChatMessageEvent(Object source, String userId, byte[] protobufMessage) {
        super(source);
        this.userId = userId;
        this.protobufMessage = protobufMessage;
    }

    public String getUserId() {
        return userId;
    }

    public byte[] getProtobufMessage() {
        return protobufMessage;
    }
}
