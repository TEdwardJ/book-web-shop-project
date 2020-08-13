package edu.ted.webshop.dao;

import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductCRUDGeneratorTest {

    @Test
    public void getFindAllQueryTest() {
        final String keyWord = "iPhone";
        String preparedQuery = ProductCRUDGenerator.getFindAllQuery(keyWord);
        assertTrue(preparedQuery.contains("SELECT product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id FROM"));
        assertTrue(preparedQuery.contains("'"+keyWord+"'"));
    }

    @Test
    public void getSelectAllQueryTest() {
        String preparedQuery = ProductCRUDGenerator.getSelectAllQuery();
        assertTrue(preparedQuery.contains("SELECT product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id FROM"));
    }
}