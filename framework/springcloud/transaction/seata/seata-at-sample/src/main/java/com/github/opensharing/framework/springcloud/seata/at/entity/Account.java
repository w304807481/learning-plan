package com.github.opensharing.framework.springcloud.seata.at.entity;

import java.math.BigDecimal;

public class Account {

    private Long id;
    private String userId;
    private BigDecimal balance;
    private BigDecimal frozenAmount;

    public Account() {
    }

    public Account(String userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
        this.frozenAmount = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", balance=" + balance +
                ", frozenAmount=" + frozenAmount +
                '}';
    }
}
