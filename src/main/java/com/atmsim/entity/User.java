package com.atmsim.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class User {
    private long id;
    private String username;
    private String password;
}
