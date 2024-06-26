package com.atmsim.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class Transaction {
    private long id;
    private Account account;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TransactionType type;
}
