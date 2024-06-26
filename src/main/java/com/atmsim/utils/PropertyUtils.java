package com.atmsim.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyUtils {
    private static final Properties properties;

    private PropertyUtils() {}

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
