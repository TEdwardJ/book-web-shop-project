package edu.ted.webshop.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertyReaderTest {

    private static final String PROPERTIES_STRING = "property1=value1\n" +
            "property2=value2\n" +
            "property3=value3";
    private static File TEST_PROPERTIES_FILE;


    @BeforeAll
    public static void init() throws IOException {
        TEST_PROPERTIES_FILE = new File("target/test-classes/test123.properties");
        TEST_PROPERTIES_FILE.createNewFile();
        FileWriter writer = new FileWriter(TEST_PROPERTIES_FILE);
        writer.write(PROPERTIES_STRING);
        writer.flush();
        writer.close();
    }

    @AfterAll
    public static void destroy(){
        TEST_PROPERTIES_FILE.deleteOnExit();
    }

    @Test
    void givenPropertyFile_whenPropertiesRead_thenCorrect() {
        Properties properties = PropertyReader.readPropertyFile("test123.properties");
        assertEquals(3, properties.size());
        assertEquals("value1", properties.getProperty("property1"));
        assertEquals("value2", properties.getProperty("property2"));
        assertEquals("value3", properties.getProperty("property3"));
    }

    @Test
    void givenNonExisttingPropertyFile_whenPropertiesHasZeroSize_thenCorrect() {
        Properties properties = PropertyReader.readPropertyFile("test321.properties");
        assertEquals(0, properties.size());
    }
}