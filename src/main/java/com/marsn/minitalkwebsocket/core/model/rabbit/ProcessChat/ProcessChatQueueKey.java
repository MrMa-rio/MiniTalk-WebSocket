package com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat;

import com.marsn.minitalkwebsocket.core.model.rabbit.QueueKey;

public record ProcessChatQueueKey(String name) implements QueueKey {
    @Override
    public String toName() {
        return name + ".queue";
    }
}
