package com.atmsim;

import com.atmsim.utils.ConnectionPool;

public class Main {
    public static void main(String[] args) {
        try (ConnectionPool pool = new ConnectionPool()) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}