package com.company.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        load();
    }

    private static void load() {
        // Classpath üzerinden 
        if (loadFromClasspath("config/test-user.properties")) return;
        if (loadFromClasspath("config/test-users.properties")) return;

        // eski davranış (relative path)
        if (loadFromFile("src/test/resources/config/test-user.properties")) return;
        if (loadFromFile("src/test/resources/config/test-users.properties")) return;

        throw new RuntimeException("Config file not found. Checked: " + "[-Dconfig.file], classpath:config/test-user(s).properties and src/test/resources/...");
    }

    private static boolean loadFromClasspath(String resourcePath) {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in != null) {
                props.load(in);
                return true;
            }
        } catch (Exception ignored) { }
        return false;
    }

    private static boolean loadFromFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            props.load(fis);
            return true;
        } catch (Exception ignored) { }
        return false;
    }

    public static String get(String key) {
        return System.getProperty(key, props.getProperty(key));
    }
}
