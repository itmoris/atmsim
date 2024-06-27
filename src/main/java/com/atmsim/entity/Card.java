package com.atmsim.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@RequiredArgsConstructor
public class Card {
    private long id;
    private long userId;
    private String number;
    private LocalDate expired;
    private String cvc;
}
