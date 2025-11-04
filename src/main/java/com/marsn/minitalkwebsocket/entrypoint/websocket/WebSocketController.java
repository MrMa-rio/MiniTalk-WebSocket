package com.marsn.minitalkwebsocket.entrypoint.websocket;

import com.marsn.minitalkwebsocket.adapter.services.consumers.ProcessConsumer;
import com.marsn.minitalkwebsocket.entrypoint.websocket.dto.ChatMessageDTO;
import com.marsn.minitalkwebsocket.entrypoint.websocket.dto.EnterChatDTO;
import com.marsn.minitalkwebsocket.v1.ChatMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {


    private final RabbitTemplate messagingTemplate;

    private final ProcessConsumer processConsumer;

    public WebSocketController(RabbitTemplate messagingTemplate, ProcessConsumer processConsumer) {
        this.messagingTemplate = messagingTemplate;
        this.processConsumer = processConsumer;
    }

    // Mapeia mensagens enviadas para /app/message
    @MessageMapping("/message")
    // Envia o retorno para todos os assinantes de /topic/messages

    public void sendMessage(ChatMessageDTO message) {

        System.out.println(message.conversationId());

        String routingKey = "process.chat." + message.conversationId(); //TODO: Criar as routingKey de maneira mais eficiente e estruturada

        messagingTemplate.convertAndSend(
                "process.exchange",
                routingKey,
                ChatMessage.newBuilder()
                        .setMessageId(message.messageId())
                        .setContent(message.content())
                        .setSenderId(message.senderId())
                        .setConversationId(message.conversationId())
                        .setTimestamp(message.timestamp())
                        .build().toByteArray()

        );


        System.out.printf("📤 [%s] Enviou mensagem na conversa %s: %s%n",
                message.senderId(), message.conversationId(), message.content());
    }

    @MessageMapping("/enter-chat")
    // Envia o retorno para todos os assinantes de /topic/messages

    public void enterChat(EnterChatDTO enterChatDTO) {


        processConsumer.subscribeToQueue(enterChatDTO.senderId(), enterChatDTO.conversationId());

        System.out.println("O USUARIO: " + enterChatDTO.senderId() + " se conectou na conversa " + enterChatDTO.conversationId());

    }
}