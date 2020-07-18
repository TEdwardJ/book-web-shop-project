package edu.ted.webshop.dao;

import edu.ted.webshop.entity.Product;
import edu.ted.webshop.utils.PropertyReader;
import edu.ted.webshop.utils.TemplateEngine;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class JdbcProductDaoTest {

    private static JdbcProductDao productDao;

    @BeforeAll
    public static void init() {
        Properties dataSourceProperties = PropertyReader.readPropertyFile("db.properties");
        final ShopDataSourceFactory dataSourceFactory = new ShopDataSourceFactory();
        dataSourceFactory.setDataSourceProperties(dataSourceProperties);
        productDao = new JdbcProductDao(dataSourceFactory, new TemplateEngine());
        Properties queries = PropertyReader.readPropertyFile("query.properties");
        productDao.setQueries(queries);
    }

    @Test
    void whenGetAllReturnsList_thenCorrect() {
        final List<Product> allProducts = productDao.getAll();
        assertFalse(allProducts.isEmpty());
    }

    @Test
    void givenKeyWordExistingInDB_whenSearchReturnsResults_thenCorrect() {
        final String keyWord = "King";
        final List<Product> allProducts = productDao.searchProducts(keyWord);
        assertFalse(allProducts.isEmpty());
        for (Product allProduct : allProducts) {
            assertTrue(allProduct.getName().concat(allProduct.getDescription()).contains(keyWord));
        }
    }

    @Test
    void givenKeyWordNonExistingInDB_whenSearchReturnsResults_thenCorrect() {
        final String keyWord = "@##@#";
        final List<Product> allProducts = productDao.searchProducts(keyWord);
        assertTrue(allProducts.isEmpty());
    }

    @Test
    void getPreparedQuery() throws IOException, TemplateException {
        Map<String, Object> parametersMap = new HashMap<>();
        final String keyWord = "iPhone";
        parametersMap.put("keyWord", keyWord);

        String queryName = "searchAll";
        String preparedQuery = productDao.getPreparedQuery(queryName, parametersMap);
        assertTrue(preparedQuery.contains("SELECT * FROM"));
        assertTrue(preparedQuery.contains("'"+keyWord+"'"));
    }

    @Test
    void givenExistingId_whenReturned_thenCorrect() {
        int productId = 24;
        final Product product = productDao.getOneById(productId);
        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("It : film tie-in edition of Stephen...", product.getName());
        assertEquals("Stephen King. It : film tie-in edition of Stephen...", product.getDescription());
        assertEquals(0, product.getPrice().compareTo(new BigDecimal(324)));
        assertFalse(product.getPictureUrl().isEmpty());
    }

    @Test
    void givenNonExistingId_whenReturned_thenCorrect() {
        int productId = 2228;
        assertNull(productDao.getOneById(productId));
    }

    @Test
    void givenExistingIdAndGetProductChangeFieldsAndUpdate_whenGetByIdReturnsUpdated_thenCorrect() {
        int productId = 28;
        final Product oldProduct = productDao.getOneById(productId);
        final String newDescription = oldProduct.getDescription() + ". New Edition";
        oldProduct.setDescription(newDescription);
        final BigDecimal newPrice = oldProduct.getPrice().add(new BigDecimal(100));
        oldProduct.setPrice(newPrice);
        productDao.updateOne(oldProduct);
        final Product product = productDao.getOneById(productId);
        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("The Bazaar of Bad Dreams Export", product.getName());
        assertEquals(newDescription, product.getDescription());
        assertEquals(newPrice, product.getPrice());
        assertFalse(product.getPictureUrl().isEmpty());
    }

    @Test
    void givenNewProductThenInsert_whenGetByIdReturnsNewProduct_thenCorrect() {
        final int productId = 2222;
        final String productName = "New Book";
        final String productDescription = "New Book Description";
        final BigDecimal productPrice = new BigDecimal(2314);
        final String pictureUrl = "http://google.com//picturetest.jpg";
        Product newProduct = new Product(productId, productName, productDescription, pictureUrl, productPrice);
        Product insertedProduct = productDao.insertOne(newProduct);

        final Product product = productDao.getOneById(insertedProduct.getId());
        assertNotNull(product);
        assertEquals(insertedProduct.getId(), product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(0, product.getPrice().compareTo(productPrice));
        assertEquals(pictureUrl, product.getPictureUrl());
    }
}