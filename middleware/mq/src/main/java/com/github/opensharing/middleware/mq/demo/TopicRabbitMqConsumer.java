package com.github.opensharing.middleware.mq.demo;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Topic RabbitMq 消费者
 *
 * @author jwen
 * Date 2020-06-14
 */
@Component
public class TopicRabbitMqConsumer {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("learning-plan-queue"),
            exchange = @Exchange(value = "learning.plan", type = ExchangeTypes.TOPIC),
            key = "${mq.topic.demo}"))
    @RabbitHandler
    public void consume(String msg) {
        System.out.println("learning-plan topic msg:" + msg);
    }
}
