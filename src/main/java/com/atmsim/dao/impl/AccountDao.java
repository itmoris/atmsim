package com.atmsim.dao.impl;

import com.atmsim.dao.Dao;
import com.atmsim.entity.Account;
import com.atmsim.utils.ConnectionPool;
import com.atmsim.utils.ResultSetUtils;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AccountDao implements Dao<Long, Account> {
    private final ConnectionPool pool;

    private static final String GET_QUERY = "SELECT * FROM accounts WHERE id = ?";
    private static final String GET_BY_CARD_QUERY = "SELECT * FROM accounts WHERE card_id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM accounts";
    private static final String SAVE_QUERY = "INSERT INTO accounts (balance, pin, card_id) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE accounts SET balance = ?, pin = ?, card_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM accounts WHERE id = ?";

    @Override
    public Optional<Account> get(Long id) {
        return getByAnyId(id, GET_QUERY);
    }

    public Optional<Account> getByCardId(Long cardId) {
        return getByAnyId(cardId, GET_BY_CARD_QUERY);
    }

    @Override
    public List<Account> getAll() {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
                ResultSet resultSet = statement.executeQuery();
        ) {
            return ResultSetUtils.of(resultSet, AccountDao::of);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long save(Account entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(SAVE_QUERY);
        ) {
            statement.setDouble(1, entity.getBalance());
            statement.setString(2, entity.getPin());
            statement.setLong(3, entity.getCardId());
            statement.executeUpdate();

            return ResultSetUtils.getGeneratedKey(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Account entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        ) {
            statement.setDouble(1, entity.getBalance());
            statement.setString(2, entity.getPin());
            statement.setLong(3, entity.getCardId());
            statement.setLong(4, entity.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Account entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
        ) {
            statement.setLong(1, entity.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Optional<Account> getByAnyId(Long id, String query) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setLong(1, id);
            @Cleanup ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Account account = of(resultSet);
                return Optional.of(account);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private static Account of(ResultSet rs) {
        return Account.builder()
                .id(rs.getLong("id"))
                .cardId(rs.getLong("card_id"))
                .balance(rs.getDouble("balance"))
                .pin(rs.getString("pin"))
                .build();
    }
}
