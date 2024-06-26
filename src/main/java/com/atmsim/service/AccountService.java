package com.atmsim.service;

import com.atmsim.entity.Account;

import java.math.BigDecimal;

public interface AccountService {
    Account create();

    void withdraw(long id, BigDecimal amount);
    void deposit(long id, BigDecimal amount);
    void transfer(long fromId, long toId, BigDecimal amount);

    void delete(long id);
}
