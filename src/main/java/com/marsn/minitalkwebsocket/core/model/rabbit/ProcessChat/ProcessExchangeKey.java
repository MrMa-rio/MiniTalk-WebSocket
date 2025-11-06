package com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat;

import com.marsn.minitalkwebsocket.core.model.rabbit.ExchangeKey;

public record ProcessExchangeKey(String name) implements ExchangeKey {
    @Override
    public String toName() {
        return name + ".exchange";
    }
}
