package com.github.opensharing.middleware.mq.demo;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

/**
 * Topic RocketMq 消费者
 *
 * @author jwen
 * Date 2020-06-14
 */
@Component
@RocketMQMessageListener(topic = "${mq.topic.demo}", consumerGroup = "${rocketmq.producer.group}")
public class TopicRocketMqConsumer {
}
