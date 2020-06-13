package com.github.opensharing.middleware.mq.model;

/**
 * 消息传输对象
 *
 * @author wenzongwei
 * Date 2020-06-13
 */
public class MessageDTO {

    private String exchange;

    private String topic;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
