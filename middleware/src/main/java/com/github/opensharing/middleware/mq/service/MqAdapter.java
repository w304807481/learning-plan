package com.github.opensharing.middleware.mq.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.github.opensharing.middleware.mq.model.MessageDTO;
import com.github.opensharing.middleware.mq.model.ResultDTO;
import com.github.opensharing.middleware.mq.provider.MqProvider;

/**
 * Mq中间件适配器
 *
 * @author wenzongwei
 * Date 2020-06-13
 */
@Component
public class MqAdapter implements MqService {
    public static final String MQ_PROVIDER_ROCKET_MQ = "RocketMq";
    public static final String MQ_PROVIDER_RABBIT_MQ = "RabbitMq";

    @Value("${mq.provider:RocketMq}")
    private String mqProvider;

    @Autowired
    private Map<String, MqProvider> mqProviderMap;

    @Override
    public ResultDTO send(MessageDTO messageDTO) {
        Assert.notEmpty(mqProviderMap, "Not any mq provider");
        MqProvider provider = mqProviderMap.get(this.getProviderKey(mqProvider));
        Assert.isNull(provider, "Not match mq provider");
        return provider.send(messageDTO);
    }

    private String getProviderKey(String mqProvider) {
        switch (mqProvider) {
            case MQ_PROVIDER_ROCKET_MQ:
                return "rabbitMqProvider";
            case MQ_PROVIDER_RABBIT_MQ:
                return "rocketMqProvider";
            default:
                return "";
        }
    }
}
