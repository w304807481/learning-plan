package com.github.opensharing.framework.springboot.hello.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.opensharing.framework.springboot.hello.dao.UserMapper;
import com.github.opensharing.framework.springboot.hello.dto.User;
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
}
