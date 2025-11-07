package com.marsn.minitalkwebsocket.adapter.services.chat.process;

import com.marsn.minitalkwebsocket.adapter.services.consumers.ProcessConsumer;
import com.marsn.minitalkwebsocket.core.model.rabbit.Exchanges;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessChatQueueKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessExchangeKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessRoutingKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.Routes;
import com.marsn.minitalkwebsocket.core.model.rabbit.RoutingKey;
import com.marsn.minitalkwebsocket.entrypoint.websocket.dto.ChatMessageDTO;
import com.marsn.minitalkwebsocket.entrypoint.websocket.dto.EnterChatDTO;
import com.marsn.minitalkwebsocket.v1.ChatMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProcessAdapter {

    private final RabbitTemplate messagingTemplate;
    private final ProcessConsumer processConsumer;
    public ProcessAdapter(RabbitTemplate messagingTemplate, ProcessConsumer processConsumer) {
        this.messagingTemplate = messagingTemplate;
        this.processConsumer = processConsumer;
    }

    public void handleMessage(ChatMessageDTO message) {

        messagingTemplate.convertAndSend(
                Exchanges.PROCESS_EXCHANGE.getExchange(),
                Routes.PROCESS_ROUTE.getRoutingKey(),
                ChatMessage.newBuilder()
                        .setMessageId(message.messageId())
                        .setContent(message.content())
                        .setSenderId(message.senderId())
                        .setConversationId(message.conversationId())
                        .setTimestamp(message.timestamp())
                        .setDestinyId(message.destinyId())
                        .build().toByteArray()

        );
    }

    public void handleEnterChat(EnterChatDTO enterChatDTO) {
        ProcessRoutingKey routingKey = new ProcessRoutingKey(enterChatDTO.senderId());
        ProcessChatQueueKey queueName = new ProcessChatQueueKey(enterChatDTO.senderId());
        ProcessExchangeKey exchangeKey = new ProcessExchangeKey(Exchanges.DELIVERY_EXCHANGE);
        processConsumer.subscribeToQueue(
                queueName,
                exchangeKey,
                routingKey);

        processConsumer.startListener(queueName);
    }
}
