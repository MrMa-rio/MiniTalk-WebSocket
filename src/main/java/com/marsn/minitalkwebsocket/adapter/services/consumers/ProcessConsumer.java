package com.marsn.minitalkwebsocket.adapter.services.consumers;


import com.marsn.minitalkwebsocket.core.model.rabbit.ExchangeKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.QueueKey;
import com.marsn.minitalkwebsocket.core.model.rabbit.RoutingKey;
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
    public void subscribeToQueue(QueueKey queueName, ExchangeKey exchangeKey, RoutingKey routingKey) {

        // Cria fila se não existir
        Queue queue = new Queue(queueName.toName(), false, true, true);
        TopicExchange exchange = new TopicExchange(exchangeKey.toName());
        rabbitAdmin.declareQueue(queue);

        // Faz o bind da fila à routing key específica
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey.toRoute()));

        // Inicia listener para a fila
//        startListener(queueName);
    }

    public void startListener(QueueKey queueName) { //TODO: Tornar esse metodo public pois ele sera utilizado para ouvir as filas
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName.toName());
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
