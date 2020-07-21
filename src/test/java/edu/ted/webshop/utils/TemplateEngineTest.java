package edu.ted.webshop.utils;

import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TemplateEngineTest {

    private static TemplateEngine TEMPLATE_ENGINE;

    private static final String BASE_TEMPLATE_PATH = "/product/";

    @BeforeAll
    public static void init() {
        TEMPLATE_ENGINE = new TemplateEngine(BASE_TEMPLATE_PATH);
        TEMPLATE_ENGINE.init();
    }

    @Test
    void getBaseTemplatePath() {
        final String baseTemplatePath = TEMPLATE_ENGINE.getBaseTemplatePath();
        assertEquals(BASE_TEMPLATE_PATH, baseTemplatePath);
    }

    @Test
    void writePage() {
        StringWriter writer = new StringWriter();
        Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("title", "Test Page Title");
        TEMPLATE_ENGINE.writePage("test.html", writer, parametersMap);
        assertTrue(writer.toString().contains("<title>Test Page Title</title>"));
    }

    @Test
    void writeString() throws IOException, TemplateException {
        String query = "UPDATE schema.table set column1 = '${column1}', column2 = '${column2}' WHERE id_column = ${id}";
        String preparedQuery = "UPDATE schema.table set column1 = 'value1', column2 = 'value2' WHERE id_column = 4488";
        Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("column1", "value1");
        parametersMap.put("column2", "value2");
        parametersMap.put("id", "4488");
        StringWriter writer = new StringWriter();
        TEMPLATE_ENGINE.writeString("Test Template", query, writer, parametersMap);
        assertEquals(preparedQuery, writer.toString());
        assertFalse(preparedQuery.contains("${"));
    }
}