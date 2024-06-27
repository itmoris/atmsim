package com.atmsim.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class ConnectionPool implements AutoCloseable {
    private final Queue<Connection> connections;
    private final List<Connection> activeConnections;
    private final int maxConnections;
    private final int minConnections;

    private static final String url = PropertyUtils.getProperty("db.url");
    private static final String driver = PropertyUtils.getProperty("db.driver");
    private static final String username = PropertyUtils.getProperty("db.username");
    private static final String password = PropertyUtils.getProperty("db.password");
    private static final int defaultMaxConnections = Integer.parseInt(PropertyUtils.getProperty("db.pool.maxConnections"));
    private static final int defaultMinConnections = Integer.parseInt(PropertyUtils.getProperty("db.pool.minConnections"));

    public ConnectionPool() throws SQLException {
        this(defaultMaxConnections, defaultMinConnections);
    }

    public ConnectionPool(int maxConnections) throws SQLException {
        this(maxConnections, defaultMinConnections);
    }

    public ConnectionPool(int maxConnections, int minConnections) throws SQLException {
        this.maxConnections = maxConnections;
        this.minConnections = minConnections;
        this.connections = new ArrayDeque<>(maxConnections);
        this.activeConnections = new ArrayList<>(maxConnections);
        initDriver();
        initConnections();
    }

    public synchronized Connection getConnection() throws SQLException {
        log.trace("Getting connection, available = {}, active = {}",
                connections.size(), activeConnections.size());

        if (size() < maxConnections) {
            Connection newConnection = createConnection();
            activeConnections.add(newConnection);
            return newConnection;
        }

        Connection connection = Objects.isNull(connections.peek())
                ? waitConnection() : connections.poll();

        activeConnections.add(connection);
        return connection;
    }

    @Override
    public void close() throws Exception {
        log.trace("Destroying connection pool, available = {}, active = {}",
                connections.size(), activeConnections.size());

        for (Connection connection : connections) {
            connection.close();
        }
    }

    private synchronized void release(Connection connection) {
        activeConnections.remove(connection);
        connections.add(connection);
        log.trace("Releasing connection, available = {}, active = {}",
                connections.size(), activeConnections.size());
        notify();
    }

    private Connection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        Class<? extends Connection> aClass = connection.getClass();
        log.trace("Created new connection, available = {}, active = {}",
                connections.size(), activeConnections.size());
        return (Connection) Proxy.newProxyInstance(
                ConnectionPool.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("close")) {
                        release((Connection) proxy);
                        return null;
                    }

                    return aClass.getMethod(
                            method.getName(),
                            method.getParameterTypes()
                    ).invoke(connection, args);
                }
        );
    }

    private Connection waitConnection() {
        try {
            while (true) {
                wait();
                Connection connection = connections.poll();
                if (Objects.nonNull(connection)) return connection;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void initDriver() {
        try {
            Class.forName(driver);
            log.info("JDBC Driver initialized: {}", driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void initConnections() throws SQLException {
        for (int i = 0; i < minConnections; i++) {
            this.connections.add(createConnection());
        }
        log.info("Connections initialized: {}", minConnections);
    }

    private int size() {
        return connections.size() + activeConnections.size();
    }
}
