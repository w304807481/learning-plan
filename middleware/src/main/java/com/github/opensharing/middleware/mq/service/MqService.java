package com.github.opensharing.middleware.mq.service;

import com.github.opensharing.middleware.mq.model.MessageDTO;
import com.github.opensharing.middleware.mq.model.ResultDTO;

/**
 * Mq服务
 *
 * @author jwen
 * Date 2020-06-13
 */
public interface MqService {

    /**
     * 发送消息
     *
     * @param messageDTO
     * @return
     */
    ResultDTO send(MessageDTO messageDTO);
}
