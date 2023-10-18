package com.epam.training.arturs_ziemelis.infrastructure;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {
    private static final Logger logger = Logger.getLogger(ConfigManager.class.getName());
    private static Properties properties;
    private ConfigManager() {
        throw new AssertionError("Cannot instantiate ConfigManager");
    }

    public static void loadProperties(String env) {
        properties = new Properties();
        String path = System.getProperty("user.dir") + "/src/test/resources/" + env + ".properties";
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while loading properties file for environment: " + env, e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
