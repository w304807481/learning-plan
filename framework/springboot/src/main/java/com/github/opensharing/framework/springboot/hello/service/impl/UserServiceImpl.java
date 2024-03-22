package com.github.opensharing.framework.springboot.hello.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.opensharing.framework.springboot.hello.dao.UserMapper;
import com.github.opensharing.framework.springboot.hello.dto.User;
import com.github.opensharing.framework.springboot.hello.service.ConfigService;
import com.github.opensharing.framework.springboot.hello.service.RedisService;
import com.github.opensharing.framework.springboot.hello.service.UserService;

/**
 * 用户服务实现类
 *
 * @author jwen
 * Date 2024/3/21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ConfigService configService;


    @Override
    public boolean updateUserById(User user) {
        //1.先删后插
        boolean delete = configService.isSwitchOn(SWITCH_USER_DELETE_BEFORE_UPDATE);
        delete = delete && this.removeById(user.getId());

        //2.更新数据库
        boolean result = delete ? this.save(user) : this.updateById(user);

        //3.Miss缓存
        result = result && redisService.removeCache(user.getName());

        //4.主动抛异常回滚事务
        if (!result) {
            throw new RuntimeException("更新用户失败");
        }

        return true;
    }
}
