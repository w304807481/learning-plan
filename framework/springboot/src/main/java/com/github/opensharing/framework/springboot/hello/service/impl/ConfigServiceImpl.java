package com.github.opensharing.framework.springboot.hello.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.opensharing.framework.springboot.hello.service.ConfigService;

/**
 * 配置服务实现类
 *
 * @author jwen
 * Date 2024/3/22
 */
@Service
public class ConfigServiceImpl  implements ConfigService {

    @Override
    public boolean isSwitchOn(String key) {
        System.out.println("从数据库获取了配置： " + key);

        //读取配置判断开关
        //.....
        return StringUtils.isNotEmpty(key);
    }
}
