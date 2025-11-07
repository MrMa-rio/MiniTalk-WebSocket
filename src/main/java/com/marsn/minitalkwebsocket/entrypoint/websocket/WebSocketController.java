package com.marsn.minitalkwebsocket.entrypoint.websocket;

import com.marsn.minitalkwebsocket.adapter.services.chat.process.ProcessAdapter;
import com.marsn.minitalkwebsocket.adapter.services.consumers.ProcessConsumer;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessChatQueueKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessExchangeKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.ProcessChat.ProcessRoutingKey;
import com.marsn.minitalkwebsocket.entrypoint.websocket.dto.ChatMessageDTO;
import com.marsn.minitalkwebsocket.entrypoint.websocket.dto.EnterChatDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {




    private final ProcessAdapter adapter;


    public WebSocketController(ProcessAdapter adapter) {
        this.adapter = adapter;
    }

    @MessageMapping("/message")
    public void sendMessage(ChatMessageDTO message) {
        adapter.handleMessage(message);
        System.out.printf("📤 [%s] Enviou mensagem na conversa %s: %s%n",
                message.senderId(), message.conversationId(), message.content());
    }

    @MessageMapping("/enter-chat")
    public void enterChat(EnterChatDTO enterChatDTO) {
        adapter.handleEnterChat(enterChatDTO);
        System.out.println("O USUARIO: " + enterChatDTO.senderId() + " se conectou ");
    }
}