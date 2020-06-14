package com.github.opensharing.middleware.mq.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.opensharing.middleware.mq.model.MessageDTO;
import com.github.opensharing.middleware.mq.service.MqService;

/**
 * MQ 生产模拟类
 *
 * @author wenzongwei
 * Date 2020-06-14
 */
@RestController
public class Producer {

    @Value("${mq.topic.demo}")
    private String mqTopic;

    @Qualifier("mqService")
    @Autowired
    private MqService mqService;

    @GetMapping("/helloworld")
    public String hellworld() {
        mqService.send(new MessageDTO("learning.plan", mqTopic, "hello world :" + System.currentTimeMillis()));
        return "success:" + System.currentTimeMillis();
    }
}
