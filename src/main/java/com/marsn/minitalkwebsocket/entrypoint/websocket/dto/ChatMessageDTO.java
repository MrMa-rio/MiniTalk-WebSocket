package com.marsn.minitalkwebsocket.entrypoint.websocket.dto;

public record ChatMessageDTO(
        String messageId,
        String conversationId,
        String senderId,
        String content,
        String timestamp,
        String destinyId
) {
}
