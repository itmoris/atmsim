package com.atmsim.service;

import com.atmsim.entity.User;

public interface UserService {
    User registration(String username, String password);

    String login(String username, String password);
}
