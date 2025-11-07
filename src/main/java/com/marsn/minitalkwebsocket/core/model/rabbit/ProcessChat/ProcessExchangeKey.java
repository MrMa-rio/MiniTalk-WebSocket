package com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat;

import com.marsn.minitalkwebsocket.core.model.rabbit.ExchangeKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.Exchanges;

public record ProcessExchangeKey(Exchanges name) implements ExchangeKey {
    @Override
    public String toName() {
        return name.getExchange();
    }
}
