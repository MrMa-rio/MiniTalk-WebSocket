package com.marsn.minitalkwebsocket.entrypoint.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    // Mapeia mensagens enviadas para /app/message
    @MessageMapping("/message")
    // Envia o retorno para todos os assinantes de /topic/messages
    @SendTo("/topic/messages")
    public String handleMessage(String message) {
        System.out.println("Mensagem recebida: " + message);
        return "Servidor: Recebi sua mensagem: '" + message + "'";
    }
}