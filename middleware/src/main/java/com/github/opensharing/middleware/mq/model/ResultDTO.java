package com.github.opensharing.middleware.mq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 结果对象
 *
 * @author wenzongwei
 * Date 2020-06-13
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultDTO {

    /**
     * MQ消息回执
     */
    private Object result;
}
