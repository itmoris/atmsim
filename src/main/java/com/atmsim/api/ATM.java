package com.atmsim.api;

import com.atmsim.entity.Card;

public interface ATM {
    void checkBalance(Card card, String pin);
    void deposit(Card card, String pin, double amount);
    void withdraw(Card card, String pin, double amount);
    void transfer(Card card, String pin, String cardNumber, double amount);
}
