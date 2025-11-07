package com.marsn.minitalkwebsocket.entrypoint.websocket;

import com.marsn.minitalkwebsocket.adapter.services.chat.process.ProcessAdapter;
import com.marsn.minitalkwebsocket.entrypoint.websocket.dto.ChatMessageDTO;
import com.marsn.minitalkwebsocket.entrypoint.websocket.dto.EnterChatDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    @SendTo("/topic/messages")
    public String enterChat(EnterChatDTO enterChatDTO) {
        adapter.handleEnterChat(enterChatDTO);
        System.out.println("O USUARIO: " + enterChatDTO.senderId() + " se conectou ");
        return "O USUARIO: " + enterChatDTO.senderId() + " se conectou ";
    }
}