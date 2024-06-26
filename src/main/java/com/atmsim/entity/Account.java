package com.atmsim.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class Account {
    private long id;
    private double balance;
    private String pin;
    private List<Transaction> transactions;
}
