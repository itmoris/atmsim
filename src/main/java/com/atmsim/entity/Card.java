package com.atmsim.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


public class Card {
    private long id;
    private String number;
    private LocalDate expired;
    private String cvc;
    private Account account;
}
