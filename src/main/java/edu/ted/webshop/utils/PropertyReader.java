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

    public static Properties readPropertyFile(String propertiesFile) throws IOException {
        logger.info("Property File To Read {}", propertiesFile);
        Properties properties = new Properties();
        final URL resource = PropertyReader.class.getClassLoader().getResource(propertiesFile);
        InputStream resourceStream = resource.openStream();
        logger.info("Property file root path {}", resource.getPath());
        properties.load(resourceStream);
        return properties;
    }
}
