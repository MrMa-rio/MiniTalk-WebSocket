package com.marsn.minitalkwebsocket;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MiniTalkWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniTalkWebsocketApplication.class, args);
    }

}
