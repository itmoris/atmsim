package com.atmsim.api;

import com.atmsim.entity.Card;
import com.atmsim.entity.Transaction;

import java.util.List;

public interface Bank {
    void registration(String username, String password);
    String login(String username, String password);

    Card createCard(String token);
    Card getCard(String token, long id);
    Card getCard(String token, String number);

    List<Transaction> getTransaction(String token, long cardId);
}
