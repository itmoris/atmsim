package com.atmsim;

import com.atmsim.utils.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (ConnectionPool pool = new ConnectionPool()) {
            for (int i = 0; i < 5; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 40; j++) {
                        try (Connection connection = pool.getConnection();) {
                            System.out.printf("%s %s %s\n", Thread.currentThread().getName(), j, connection);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}