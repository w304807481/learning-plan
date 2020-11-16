package com.github.opensharing.middleware.mq.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息传输对象
 *
 * @author jwen
 * Date 2020-06-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO implements Serializable {

    private String exchange;

    private String topic;

    private Object msg;
}
