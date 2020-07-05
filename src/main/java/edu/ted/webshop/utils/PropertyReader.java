package edu.ted.webshop.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    public static Properties readPropertyFile(String propertiesFile) throws IOException {
        Properties properties = new Properties();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String dbConfigPath = rootPath + propertiesFile;

        properties.load(new FileInputStream(dbConfigPath));
        return properties;
    }
}
