package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config/config.properties");
            if (is == null) {
                throw new RuntimeException("config.properties file not found in config/");
            }
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
