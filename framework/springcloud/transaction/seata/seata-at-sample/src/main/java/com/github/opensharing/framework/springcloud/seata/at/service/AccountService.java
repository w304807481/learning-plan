package com.github.opensharing.framework.springcloud.seata.at.service;

import com.github.opensharing.framework.springcloud.seata.at.entity.Account;

import java.math.BigDecimal;

/**
 * 账户服务接口（AT 模式）
 */
public interface AccountService {

    /**
     * 扣减账户余额
     *
     * @param userId  用户ID
     * @param amount  扣减金额
     * @param error   是否模拟业务异常（true=扣减成功后主动抛异常，用于验证Seata回滚）
     */
    void debit(String userId, BigDecimal amount, boolean error);

    /**
     * 查询账户信息
     *
     * @param userId 用户ID
     * @return 账户信息
     */
    Account getAccount(String userId);
}
