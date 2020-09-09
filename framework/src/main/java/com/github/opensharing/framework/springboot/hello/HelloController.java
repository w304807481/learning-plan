package com.github.opensharing.framework.springboot.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 *
 * @author wenzongwei
 * Date 2020-09-09
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "wolrld";
    }
}
