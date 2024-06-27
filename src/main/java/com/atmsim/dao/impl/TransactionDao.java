package com.atmsim.dao.impl;

import com.atmsim.dao.Dao;
import com.atmsim.entity.Transaction;
import com.atmsim.entity.TransactionType;
import com.atmsim.utils.ConnectionPool;
import com.atmsim.utils.ResultSetUtils;
import com.atmsim.utils.TimeUtils;
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
public class TransactionDao implements Dao<Long, Transaction> {
    private final ConnectionPool pool;

    private static final String GET_QUERY = "SELECT * FROM transactions WHERE id = ?";
    private static final String GET_BY_ACCOUNT_QUERY = "SELECT * FROM transactions WHERE account_id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM transactions";
    private static final String SAVE_QUERY = "INSERT INTO transactions (account_id, start_time, end_time, type) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE transactions SET account_id = ?, start_time = ?, end_time = ?, type = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM transactions WHERE id = ?";

    @Override
    public Optional<Transaction> get(Long id) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setLong(1, id);
            @Cleanup ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Transaction transaction = of(resultSet);
                return Optional.of(transaction);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transaction> getByAccountId(Long accountId) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_BY_ACCOUNT_QUERY)
        ) {
            statement.setLong(1, accountId);
            @Cleanup ResultSet resultSet = statement.executeQuery();
            return ResultSetUtils.of(resultSet, TransactionDao::of);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> getAll() {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
                ResultSet resultSet = statement.executeQuery()
        ) {
            return ResultSetUtils.of(resultSet, TransactionDao::of);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long save(Transaction entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)
        ) {
            statement.setLong(1, entity.getAccountId());
            statement.setTimestamp(2, TimeUtils.of(entity.getStartTime()));
            statement.setTimestamp(3, TimeUtils.of(entity.getEndTime()));
            statement.setString(4, entity.getType().name());
            statement.executeUpdate();

            return ResultSetUtils.getGeneratedKey(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Transaction entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setLong(1, entity.getAccountId());
            statement.setTimestamp(2, TimeUtils.of(entity.getStartTime()));
            statement.setTimestamp(3, TimeUtils.of(entity.getEndTime()));
            statement.setString(4, entity.getType().name());
            statement.setLong(5, entity.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Transaction entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            statement.setLong(4, entity.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private static Transaction of(ResultSet resultSet) {
        return Transaction.builder()
                .id(resultSet.getLong("id"))
                .accountId(resultSet.getLong("account_id"))
                .startTime(TimeUtils.of(resultSet.getTimestamp("start_time")))
                .endTime(TimeUtils.of(resultSet.getTimestamp("end_time")))
                .type(TransactionType.valueOf(resultSet.getString("type")))
                .build();
    }
}
