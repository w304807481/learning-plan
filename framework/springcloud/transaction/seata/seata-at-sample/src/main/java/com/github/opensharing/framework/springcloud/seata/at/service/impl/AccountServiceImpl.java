package com.github.opensharing.framework.springcloud.seata.at.service.impl;

import com.github.opensharing.framework.springcloud.seata.at.entity.Account;
import com.github.opensharing.framework.springcloud.seata.at.mapper.AccountMapper;
import com.github.opensharing.framework.springcloud.seata.at.service.AccountService;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 账户服务实现（AT 模式）
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    @GlobalTransactional(name = "debit-tx", rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void debit(String userId, BigDecimal amount, boolean error) {
        log.info("开始扣款 - userId: {}, amount: {}, 模拟异常: {}", userId, amount, error);

        // 1. 查询账户
        Account account = accountMapper.selectByUserId(userId);
        if (account == null) {
            throw new RuntimeException("账户不存在: " + userId);
        }

        // 2. 检查余额
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足，当前余额: " + account.getBalance());
        }

        // 3. 扣减余额（Seata AT 模式会自动生成 undo log，注册分支、获取全局锁）
        int rows = accountMapper.debit(userId, amount);
        if (rows == 0) {
            throw new RuntimeException("扣款失败，更新行数为 0");
        }

        BigDecimal balanceAfterDebit = account.getBalance().subtract(amount);
        log.info("扣款完成（本地事务已提交，undo_log已写入，分支已注册） - userId: {}, 扣款金额: {}, 剩余余额: {}",
                userId, amount, balanceAfterDebit);

        // 4.【关键】扣减成功后再抛异常：验证 Seata 根据 undo_log 反向补偿（真正的全局回滚）
        //    此时本地事务已 commit，余额已真的扣减；抛异常后 Seata TC 下发 Rollback，
        //    RM 用 before-image 执行反向 UPDATE 把余额恢复为原值。
        if (error) {
            log.info("模拟业务异常触发 Seata 全局回滚：抛出 RuntimeException，期望余额最终由 {} 恢复为 {}",
                    balanceAfterDebit, account.getBalance());
            throw new RuntimeException("模拟业务异常，触发Seata全局回滚(undo_log补偿)");
        }
    }

    @Override
    public Account getAccount(String userId) {
        log.info("查询账户 - userId: {}", userId);
        return accountMapper.selectByUserId(userId);
    }
}
