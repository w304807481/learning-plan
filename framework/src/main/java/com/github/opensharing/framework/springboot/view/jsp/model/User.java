package com.github.opensharing.framework.springboot.view.jsp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User
 *
 * @author jwen
 * Date 2020-09-17
 */
@AllArgsConstructor
@Data
public class User {
    private int id;
    private String name;
    private int age;
}
