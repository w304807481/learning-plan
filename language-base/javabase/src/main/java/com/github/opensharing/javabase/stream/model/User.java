package com.github.opensharing.javabase.stream.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * User
 *
 * @author jwen
 * Date 2020-09-20
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;
    private String name;
    private int age;
    private String sex;

    public boolean isFemale() {
        return "女".equals(sex);
    }
}
