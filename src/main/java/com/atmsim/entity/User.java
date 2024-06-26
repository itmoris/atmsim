package com.atmsim.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class User {
    private long id;
    private String username;
    private String password;
    private List<Card> cards;
}
