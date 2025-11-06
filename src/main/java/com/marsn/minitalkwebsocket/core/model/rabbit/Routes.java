package com.marsn.minitalkwebsocket.core.model.rabbit;

public enum Routes {


    PROCESS_ROUTE("chat.process.message");

    private final String routingKey;

    Routes(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getRoutingKey() {
        return this.routingKey;
    }



}
