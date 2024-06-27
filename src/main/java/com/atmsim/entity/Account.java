package com.atmsim.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class Account {
    private final long id;
    private final long cardId;
    private final double balance;
    private final String pin;
}
