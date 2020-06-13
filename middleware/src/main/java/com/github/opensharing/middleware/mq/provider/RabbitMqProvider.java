package com.github.opensharing.middleware.mq.provider;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.opensharing.middleware.mq.model.MessageDTO;
import com.github.opensharing.middleware.mq.model.ResultDTO;

/**
 * RabbitMq
 *
 * @author wenzongwei
 * Date 2020-06-13
 */
@Component("rabbitMqProvider")
public class RabbitMqProvider implements MqProvider {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResultDTO send(MessageDTO messageDTO) {
        //TODO 根据messageDTO采用不同的send方式
        rabbitTemplate.convertAndSend(messageDTO.getExchange(), messageDTO.getTopic(), messageDTO);
        return new ResultDTO();
    }
}
