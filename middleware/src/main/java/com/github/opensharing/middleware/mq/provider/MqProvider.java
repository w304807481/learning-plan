package com.github.opensharing.middleware.mq.provider;

import com.github.opensharing.middleware.mq.model.MessageDTO;
import com.github.opensharing.middleware.mq.model.ResultDTO;

/**
 * MqProvider
 *
 * @author wenzongwei
 * Date 2020-06-13
 */
public interface MqProvider {

    /**
     * @param messageDTO
     * @return
     */
    ResultDTO send(MessageDTO messageDTO);
}
