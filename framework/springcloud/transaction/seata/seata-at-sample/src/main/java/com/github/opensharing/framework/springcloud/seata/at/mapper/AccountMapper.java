package com.github.opensharing.framework.springcloud.seata.at.mapper;

import com.github.opensharing.framework.springcloud.seata.at.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 账户 Mapper
 */
@Mapper
public interface AccountMapper {

    /**
     * 根据用户ID查询账户
     */
    @Select("SELECT id, user_id AS userId, balance, frozen_amount AS frozenAmount FROM account WHERE user_id = #{userId}")
    Account selectByUserId(@Param("userId") String userId);

    /**
     * 扣减余额
     */
    @Update("UPDATE account SET balance = balance - #{amount}, update_time = NOW() WHERE user_id = #{userId} AND balance >= #{amount}")
    int debit(@Param("userId") String userId, @Param("amount") BigDecimal amount);
}
