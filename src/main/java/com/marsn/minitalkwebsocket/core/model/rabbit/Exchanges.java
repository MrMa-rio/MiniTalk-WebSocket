package com.marsn.minitalkwebsocket.core.model.rabbit;

public enum Exchanges {


    PROCESS_EXCHANGE("chat.process.exchange"),
    DELIVERY_EXCHANGE("chat.delivery.exchange");

    private final String exchange;

    Exchanges(String exchange) {
        this.exchange = exchange;
    }

    public String getExchange() {
        return this.exchange;
    }



}
