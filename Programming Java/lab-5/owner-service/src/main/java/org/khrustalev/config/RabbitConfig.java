package org.khrustalev.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String OWNER_SAVE_QUEUE   = "rpc.owner.save";
    public static final String OWNER_UPDATE_QUEUE = "rpc.owner.update";
    public static final String OWNER_GET_QUEUE    = "rpc.owner.get";
    public static final String OWNER_LIST_QUEUE   = "rpc.owner.list";
    public static final String OWNER_PAGE_QUEUE   = "rpc.owner.page";
    public static final String OWNER_DELETE_QUEUE = "rpc.owner.delete";

    @Bean Queue ownerSaveQueue()   { return new Queue(OWNER_SAVE_QUEUE, true); }
    @Bean Queue ownerUpdateQueue() { return new Queue(OWNER_UPDATE_QUEUE, true); }
    @Bean Queue ownerGetQueue()    { return new Queue(OWNER_GET_QUEUE, true); }
    @Bean Queue ownerListQueue()   { return new Queue(OWNER_LIST_QUEUE, true); }
    @Bean Queue ownerPageQueue()   { return new Queue(OWNER_PAGE_QUEUE, true); }
    @Bean Queue ownerDeleteQueue() { return new Queue(OWNER_DELETE_QUEUE, true); }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter jsonConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonConverter);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
