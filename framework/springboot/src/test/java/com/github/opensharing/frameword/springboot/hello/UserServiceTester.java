package com.github.opensharing.frameword.springboot.hello;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.opensharing.framework.springboot.hello.dao.UserMapper;
import com.github.opensharing.framework.springboot.hello.dto.User;
import com.github.opensharing.framework.springboot.hello.service.ConfigService;
import com.github.opensharing.framework.springboot.hello.service.RedisService;
import com.github.opensharing.framework.springboot.hello.service.UserService;
import com.github.opensharing.framework.springboot.hello.service.impl.UserServiceImpl;

/**
 * 用户服务测试类： 测试业务逻辑正确性
 *
 * @author jwen
 * Date 2024/3/21
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {App.class})
public class UserServiceTester {

    @Mock
    private UserMapper userMapper;

    @Mock
    private RedisService redisService;

    @Mock
    private ConfigService configService;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserById_假如数据库存在ID为1的数据_当执行查询时_那么应该返回数据() {
        when(userMapper.selectById(1)).thenReturn(new User());

        User user = userService.getById(1);
        Assert.assertNotNull(user);
    }

    @Test
    public void testGetUserById_假如数据库不存在ID为1的数据_当执行查询时_那么应该返回空() {
        when(userMapper.selectById(any())).thenReturn(null);

        User user = userService.getById(1);
        Assert.assertNull(user);
    }

    @Test
    public void testUpdateUserById_假如先删除后更新开关打开且更新缓存无异常_当执行更新_那么应该采取新增() {

        when(configService.isSwitchOn(UserService.SWITCH_USER_DELETE_BEFORE_UPDATE)).thenReturn(true);
        when(redisService.removeCache("李四")).thenReturn(true);
        when(userMapper.insert(any())).thenReturn(1);
        when(userMapper.deleteById(any())).thenReturn(1);
        when(userMapper.updateById(any())).thenReturn(1);

        User user = new User(1, "李四", 30, "123456@qq.com");

        boolean result = userService.updateUserById(user);

        Assert.assertTrue(result);
        verify(userMapper, times(1)).deleteById(any());
        verify(userMapper, times(1)).insert(any());
        verify(userMapper, never()).updateById(any());
        verify(redisService, times(1)).removeCache(any());
    }
}
