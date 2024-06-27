package com.atmsim.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
public class Transaction {
    private final long id;
    private final long accountId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final TransactionType type;
}
