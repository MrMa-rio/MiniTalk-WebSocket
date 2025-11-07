package com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat;

import com.marsn.minitalkwebsocket.core.model.rabbit.RoutingKey;

public record ProcessRoutingKey(String name) implements RoutingKey {
    @Override
    public String toRoute() {
        return "process.chat." + name;
    }
}
