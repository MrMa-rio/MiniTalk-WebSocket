package com.marsn.minitalkwebsocket.adapter.services.consumers;


import com.google.protobuf.InvalidProtocolBufferException;
import com.marsn.minitalkwebsocket.adapter.services.chat.process.DeliveryAdapter;
import com.marsn.minitalkwebsocket.core.model.rabbit.ExchangeKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.QueueKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.RoutingKey;
import com.marsn.minitalkwebsocket.v1.ChatMessage;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;


@Service
public class GenericConsumer {

    private final RabbitAdmin rabbitAdmin;
    private final ConnectionFactory connectionFactory;


    private final DeliveryAdapter adapter;

    public GenericConsumer(RabbitAdmin rabbitAdmin, ConnectionFactory connectionFactory, DeliveryAdapter adapter) {
        this.rabbitAdmin = rabbitAdmin;
        this.connectionFactory = connectionFactory;
        this.adapter = adapter;
    }

    public void subscribeToDynamicQueue(QueueKey queueName, ExchangeKey exchangeKey, RoutingKey routingKey) {
        Queue queue = new Queue(queueName.toName(), false, true, true);
        TopicExchange exchange = new TopicExchange(exchangeKey.toName());
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey.toRoute()));

        startListener(queueName);

    }

    private void startListener(QueueKey queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName.toName());
        container.setMessageListener(this::handleReceivedMessage);
        container.start();
    }

    private void handleReceivedMessage(Message message) {
        try {
                ChatMessage chatMsg = ChatMessage.parseFrom(message.getBody());
                System.out.println("Mensagem de " + chatMsg.getSenderId() + " para " + chatMsg.getDestinyId());
                adapter.handleMessageReceived(chatMsg);
        } catch (InvalidProtocolBufferException e) {
           adapter.handleMessageError(message);
        }
    }
}
