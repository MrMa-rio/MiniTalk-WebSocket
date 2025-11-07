package com.marsn.minitalkwebsocket.adapter.configuration;

import com.marsn.minitalkwebsocket.core.model.rabbit.Exchanges;
import com.marsn.minitalkwebsocket.core.model.rabbit.Queues;
import com.marsn.minitalkwebsocket.core.model.rabbit.Routes;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {


    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean
    public Queue processQueue() {
        return new Queue(Queues.PROCESS_QUEUE.getQueue(), false);
    }

    @Bean
    public DirectExchange processExchange() {
        return new DirectExchange(Exchanges.PROCESS_EXCHANGE.getExchange());
    }

    @Bean
    public TopicExchange deliveryExchange() {
        return new TopicExchange(Exchanges.DELIVERY_EXCHANGE.getExchange());
    }

    @Bean
    public Binding processBinding(Queue processQueue, DirectExchange processExchange) {
        return BindingBuilder
                .bind(processQueue)
                .to(processExchange)
                .with(Routes.PROCESS_ROUTE.getRoutingKey());
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }




    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
