package com.github.opensharing.framework.springboot.hello.service.impl;

import org.springframework.stereotype.Service;

import com.github.opensharing.framework.springboot.hello.service.RedisService;

/**
 *  Redis服务实现类
 *
 * @author jwen
 * Date 2024/3/21
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Override
    public boolean removeCache(String key) {
        System.out.println("已成功清理缓存：" + key);
        return true;
    }
}
