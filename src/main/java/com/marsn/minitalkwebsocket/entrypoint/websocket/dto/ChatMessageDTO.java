package com.marsn.minitalkwebsocket.entrypoint.websocket.dto;

public record ChatMessageDTO(
        String sender,
        String content
) {
}
