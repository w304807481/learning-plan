package com.github.opensharing.framework.springboot.hello.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 用户实体
 *
 * @author jwen
 * Date 2024/3/21
 */
@ToString
@Data
public class User {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 用户邮箱
     */
    private String email;
}
