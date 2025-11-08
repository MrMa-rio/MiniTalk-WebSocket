package com.marsn.minitalkwebsocket.adapter.services.chat.process;

import com.marsn.minitalkwebsocket.core.model.rabbit.Exchanges;
import com.marsn.minitalkwebsocket.core.model.rabbit.Routes;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;

@Service
public class ProcessAdapter {

    private final RabbitTemplate messagingTemplate;

    public ProcessAdapter(RabbitTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void handleSendMessage(DataBuffer message) {
        try {
            byte[] messageBytes = new byte[message.readableByteCount()];
            message.read(messageBytes);
            messagingTemplate.convertAndSend(
                    Exchanges.PROCESS_EXCHANGE.getExchange(),
                    Routes.PROCESS_ROUTE.getRoutingKey(),
                    messageBytes
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar DataBuffer", e);
        }
    }

}
