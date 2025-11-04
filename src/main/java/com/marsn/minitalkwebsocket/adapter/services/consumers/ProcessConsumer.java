package com.marsn.minitalkwebsocket.adapter.services.consumers;


import com.marsn.minitalkwebsocket.v1.ChatMessage;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class ProcessConsumer {

    private final RabbitAdmin rabbitAdmin;
    private final ConnectionFactory connectionFactory;

    public ProcessConsumer( RabbitAdmin rabbitAdmin, ConnectionFactory connectionFactory) {
        this.rabbitAdmin = rabbitAdmin;
        this.connectionFactory = connectionFactory;
    }

    /**
     * Cria dinamicamente uma fila temporária e faz o bind na conversa desejada.
     */
    public void subscribeToConversation(String userId, String conversationId) {
        String queueName = "process.queue." + userId;
        TopicExchange exchange = new TopicExchange("process.exchange");

        // Cria fila se não existir
        Queue queue = new Queue(queueName, false, true, true);
        rabbitAdmin.declareQueue(queue);

        // Faz o bind da fila à routing key específica
        String routingKey = "process.chat." + conversationId;
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey));

        // Inicia listener para a fila
        startListener(queueName);
    }

    private void startListener(String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(message -> {
            try {
                ChatMessage chatMsg = ChatMessage.parseFrom(message.getBody());
                System.out.println(queueName);
                System.out.printf("📩 [%s] Nova mensagem em %s: %s%n",
                        chatMsg.getSenderId(), chatMsg.getConversationId(), chatMsg.getContent());

            } catch (Exception e) {
                throw new RuntimeException("ERRO AO CONSUMIR AS MENSAGENS DO CHAT");
            }
        });
        container.start();
    }
}
