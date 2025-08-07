package com.github.opensharing.framework.springboot.hello.service;

/**
 * 配置服务
 *
 * @author jwen
 * Date 2024/3/22
 */
public interface ConfigService {

    /**
     * 获取开关状态
     *
     * @param key
     * @return
     */
    boolean isSwitchOn(String key);
}
