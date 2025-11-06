package com.marsn.minitalkwebsocket.core.model.rabbit;

public enum Queues {
    PROCESS_QUEUE("chat.process.queue");


    private final String queue;

    Queues(String queue) {
        this.queue = queue;
    }

    public String getQueue() {
        return this.queue;
    }
}
