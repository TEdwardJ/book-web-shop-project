package edu.ted.webshop.utils;

import edu.ted.webshop.utils.ProductCRUDGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCRUDGeneratorTest {

    @Test
    public void getFindAllQueryTest() {
        String keyWord = "iPhone";
        String preparedQuery = ProductCRUDGenerator.getFindAllQuery(keyWord);
        assertTrue(preparedQuery.contains("SELECT product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id FROM"));
        assertTrue(preparedQuery.contains("'"+keyWord+"'"));
    }

    @Test
    public void getSelectAllQueryTest() {
        String preparedQuery = ProductCRUDGenerator.getSelectAllQuery();
        assertTrue(preparedQuery.contains("SELECT product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id FROM"));
    }

    @Test
    public void givenTextWithQuote_whenReturnsQuoteReplaced_thenCorrect() {
        String initialLine ="some text ' and some text again";
        String processedLine = ProductCRUDGenerator.quoteReplace(initialLine);
        assertEquals("some text '' and some text again", processedLine);
    }
}