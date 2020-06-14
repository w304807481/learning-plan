package com.github.opensharing.middleware.mq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.opensharing.middleware.mq.model.MessageDTO;
import com.github.opensharing.middleware.mq.model.ResultDTO;

/**
 * Mq服务实现
 *
 * @author wenzongwei
 * Date 2020-06-13
 */
@Service("mqService")
public class MqServiceImpl implements MqService {

    @Autowired
    private MqAdapter mqAdapter;

    @Override
    public ResultDTO send(MessageDTO messageDTO) {
        return mqAdapter.send(messageDTO);
    }
}
