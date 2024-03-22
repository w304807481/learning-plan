package com.github.opensharing.framework.springboot.hello.service;

/**
 * Redis服务类
 *
 * @author jwen
 * Date 2024/3/21
 */
public interface RedisService {

    /**
     * 根据缓存KEY清理缓存
     *
     * @param key
     * @return
     */
    boolean removeCache(String key);
}
