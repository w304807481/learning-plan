package com.github.opensharing.frameword.springboot.hello;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.opensharing.framework.springboot.hello.App;
import com.github.opensharing.framework.springboot.hello.dao.UserMapper;
import com.github.opensharing.framework.springboot.hello.dto.User;

/**
 * 用户Mapper测试类： 主要测试SQL执行正确性
 *
 * @author jwen
 * Date 2024/3/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
public class UserMapperTester {

    @Autowired
    private UserMapper userMapper;

    @Before
    public void init() {
        System.out.println("Every case Before");
    }

    @Transactional
    @Test
    public void testInsert() {
        User user = new User(0,"张三", 23, "123456@qq.com");
        int result = userMapper.insert(user);
        Assert.assertEquals(result, 1);
    }

    @Test
    public void testGetUserById() {
        User user = userMapper.selectById(1);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getId(), Integer.valueOf(1));
    }

    @Transactional
    @Test
    public void testUpdateById() {
        User user = new User(1,"张三", 23, "123456@qq.com");
        int result = userMapper.updateById(user);
        Assert.assertEquals(result, 1);
    }

    @Transactional
    @Test
    public void testDeleteById() {
        int result = userMapper.deleteById(1);
        Assert.assertEquals(result, 1);
    }

    @After
    public void destory() {
        System.out.println("Every case After");
    }
}
