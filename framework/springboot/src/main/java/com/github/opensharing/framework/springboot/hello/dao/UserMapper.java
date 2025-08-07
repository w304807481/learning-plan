package com.github.opensharing.framework.springboot.hello.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.opensharing.framework.springboot.hello.dto.User;

/**
 * 用户数据访问对象
 *
 * @author jwen
 * Date 2024/3/21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
