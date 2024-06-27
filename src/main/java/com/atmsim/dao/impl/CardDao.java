package com.atmsim.dao.impl;

import com.atmsim.dao.Dao;
import com.atmsim.entity.Card;
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
public class CardDao implements Dao<Long, Card> {
    private final ConnectionPool pool;

    private static final String GET_QUERY = "SELECT * FROM cards WHERE id = ?";
    private static final String GET_BY_USER_QUERY = "SELECT * FROM cards WHERE user_id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM cards";
    private static final String SAVE_QUERY = "INSERT INTO cards (user_id, number, expired, cvc) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE cards SET user_id = ?, number = ?, expired = ?, cvc = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM cards WHERE id = ?";

    @Override
    public Optional<Card> get(Long id) {
        return getByAnyId(id, GET_QUERY);
    }

    public Optional<Card> getByUserId(Long userId) {
        return getByAnyId(userId, GET_BY_USER_QUERY);
    }

    @Override
    public List<Card> getAll() {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
                ResultSet resultSet = statement.executeQuery();
        ) {
            return ResultSetUtils.of(resultSet, CardDao::of);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long save(Card entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(SAVE_QUERY);
        ) {
            statement.setLong(1, entity.getUserId());
            statement.setString(2, entity.getNumber());
            statement.setDate(3, TimeUtils.of(entity.getExpired()));
            statement.setString(4, entity.getCvc());
            statement.executeUpdate();

            return ResultSetUtils.getGeneratedKey(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Card entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        ) {
            statement.setLong(1, entity.getUserId());
            statement.setString(2, entity.getNumber());
            statement.setDate(3, TimeUtils.of(entity.getExpired()));
            statement.setString(4, entity.getCvc());
            statement.setLong(5, entity.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(Card entity) {
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

    private Optional<Card> getByAnyId(Long id, String query) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setLong(1, id);
            @Cleanup ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Card card = of(resultSet);
                return Optional.of(card);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private static Card of(ResultSet rs) {
        return Card.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .number(rs.getString("number"))
                .expired(TimeUtils.of(rs.getDate("expired")))
                .cvc(rs.getString("cvc"))
                .build();
    }
}
