package com.github.opensharing.middleware.mq.provider;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.github.opensharing.middleware.mq.model.MessageDTO;
import com.github.opensharing.middleware.mq.model.ResultDTO;

/**
 * RocketMq
 *
 * @author wenzongwei
 * Date 2020-06-13
 */
@ConditionalOnProperty(name = "rocketmq.name-server")
@Component("rocketMqProvider")
public class RocketMqProvider implements MqProvider {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public ResultDTO send(MessageDTO messageDTO) {
        //TODO 根据messageDTO采用不同的send方式
        SendResult sendResult = rocketMQTemplate.syncSend(messageDTO.getTopic(), messageDTO.getMsg());
        return new ResultDTO(sendResult);
    }
}
