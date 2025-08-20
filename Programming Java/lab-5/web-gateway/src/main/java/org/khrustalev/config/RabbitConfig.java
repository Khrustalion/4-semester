package org.khrustalev.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    public static final String PET_SAVE_QUEUE       = "rpc.pet.save";
    public static final String PET_UPDATE_QUEUE     = "rpc.pet.update";
    public static final String PET_GET_QUEUE        = "rpc.pet.get";
    public static final String PET_DELETE_QUEUE     = "rpc.pet.delete";
    public static final String PET_LIST_QUEUE       = "rpc.pet.list";
    public static final String PET_PAGE_QUEUE       = "rpc.pet.page";
    public static final String PET_ADD_FRIEND_QUEUE = "rpc.pet.addFriend";

    public static final String OWNER_SAVE_QUEUE   = "rpc.owner.save";
    public static final String OWNER_UPDATE_QUEUE = "rpc.owner.update";
    public static final String OWNER_GET_QUEUE    = "rpc.owner.get";
    public static final String OWNER_DELETE_QUEUE = "rpc.owner.delete";
    public static final String OWNER_PAGE_QUEUE   = "rpc.owner.page";

    @Bean Queue petSaveQueue()       { return new Queue(PET_SAVE_QUEUE, true); }
    @Bean Queue petUpdateQueue()     { return new Queue(PET_UPDATE_QUEUE, true); }
    @Bean Queue petGetQueue()        { return new Queue(PET_GET_QUEUE, true); }
    @Bean Queue petDeleteQueue()     { return new Queue(PET_DELETE_QUEUE, true); }
    @Bean Queue petPageQueue()       { return new Queue(PET_PAGE_QUEUE, true); }
    @Bean Queue petAddFriendQueue()  { return new Queue(PET_ADD_FRIEND_QUEUE, true); }

    @Bean Queue ownerSaveQueue()     { return new Queue(OWNER_SAVE_QUEUE, true); }
    @Bean Queue ownerUpdateQueue()   { return new Queue(OWNER_UPDATE_QUEUE, true); }
    @Bean Queue ownerGetQueue()      { return new Queue(OWNER_GET_QUEUE, true); }
    @Bean Queue ownerDeleteQueue()   { return new Queue(OWNER_DELETE_QUEUE, true); }
    @Bean Queue ownerListQueue()     { return new Queue(OWNER_PAGE_QUEUE, true); }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
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
