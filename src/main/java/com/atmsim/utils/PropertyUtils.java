package com.atmsim.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyUtils {
    private static final Properties properties;

    static {
        ClassLoader classLoader = PropertyUtils.class.getClassLoader();

        try (InputStream ras = classLoader.getResourceAsStream("application.properties")) {
            properties = new Properties();
            properties.load(ras);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
