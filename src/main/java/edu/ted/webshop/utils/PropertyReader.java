package edu.ted.webshop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertyReader {
    private static Logger logger = LoggerFactory.getLogger(PropertyReader.class);

    public static Properties readPropertyFile(String propertiesFile) {
        logger.info("Property File To Read {}", propertiesFile);
        Properties properties = new Properties();
        final URL resource = PropertyReader.class.getClassLoader().getResource(propertiesFile);
        if (resource == null) {
            logger.warn("Resource {} not found {}", propertiesFile);
            return properties;
        }
        logger.info("Property file {}", resource.getPath());
        try {
            InputStream resourceStream = resource.openStream();
            properties.load(resourceStream);
        } catch (IOException e) {
            logger.error("Property cannot be read since some error happend {}", e);
            logger.error("Since the property file {} has not been read this Reader returns empty property set", propertiesFile);
        }
        return properties;
    }
}
