package com.atmsim.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultSetUtils {
    public static long getGeneratedKey(Statement statement) throws SQLException {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
        }
        return -1;
    }

    public static <T> List<T> of(ResultSet rs, Function<ResultSet, T> supplier) throws SQLException {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            list.add(supplier.apply(rs));
        }
        return list;
    }
}
