package com.marsn.minitalkwebsocket.entrypoint.websocket;

import com.marsn.minitalkwebsocket.entrypoint.websocket.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        return message;
    }
}