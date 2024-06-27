package com.atmsim.dao.impl;

import com.atmsim.dao.Dao;
import com.atmsim.entity.Card;
import com.atmsim.entity.User;
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
public class UserDao implements Dao<Long, User> {
    private final ConnectionPool pool;

    private static final String GET_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM users";
    private static final String SAVE_QUERY = "INSERT INTO users (username, password) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET username = ?, password = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";

    @Override
    public Optional<User> get(Long id) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_QUERY);
        ) {
            statement.setLong(1, id);
            @Cleanup ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = of(resultSet);
                return Optional.of(user);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAll() {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
                ResultSet resultSet = statement.executeQuery();
        ) {
            return ResultSetUtils.of(resultSet, UserDao::of);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long save(User entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(SAVE_QUERY);
        ) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.executeUpdate();

            return ResultSetUtils.getGeneratedKey(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(User entity) {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        ) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(User entity) {
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

    @SneakyThrows
    private static User of(ResultSet rs) {
        return User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .build();
    }
}
