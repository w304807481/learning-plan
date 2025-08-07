package com.github.opensharing.framework.springboot.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.opensharing.framework.springboot.hello.dto.User;

/**
 * 用户服务接口
 *
 * @author jwen
 * Date 2024/3/21
 */
public interface UserService extends IService<User> {

    String SWITCH_USER_DELETE_BEFORE_UPDATE = "SWITCH_USER_DELETE_BEFORE_UPDATE";

    /**
     * 批量保存用户并更新缓存
     *
     * @param user
     * @return
     */
    boolean updateUserById(User user);
}
