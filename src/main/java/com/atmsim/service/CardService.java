package com.atmsim.service;

import com.atmsim.entity.Card;

import java.util.List;

public interface CardService {
    Card create(String token, String pin);

    void remove(long id);

    List<Card> getAllCards();

    void getCard(long id);

    void getCard(String number);
}
