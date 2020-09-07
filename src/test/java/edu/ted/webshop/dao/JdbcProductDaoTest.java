package edu.ted.webshop.dao;

import edu.ted.webshop.entity.Product;
import edu.ted.webshop.utils.FreeMarkerTemplateEngine;
import edu.ted.webshop.utils.PropertyReader;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcProductDaoTest {

    private static JdbcProductDao productDao;

    @BeforeAll
    public static void init() {
        Properties dataSourceProperties = PropertyReader.readPropertyFile("db.properties");
        JdbcDataSourceFactory dataSourceFactory = new JdbcDataSourceFactory(dataSourceProperties);
        productDao = new JdbcProductDao(dataSourceFactory.getDataSource());
        Properties queries = PropertyReader.readPropertyFile("query.properties");
    }

    @Test
    public void whenGetAllReturnsList_thenCorrect() {
        List<Product> allProducts = productDao.getAll();
        assertFalse(allProducts.isEmpty());
    }

    @Test
    public void whenGetAllAndSomeException_when_DataException_thenCorrect() {
        //when()
        List<Product> allProducts = productDao.getAll();
        assertFalse(allProducts.isEmpty());
    }

    @Test
    public void givenKeyWordExistingInDB_whenSearchReturnsResults_thenCorrect() {
        String keyWord = "King";
        List<Product> allProducts = productDao.searchProducts(keyWord);
        assertFalse(allProducts.isEmpty());
        for (Product allProduct : allProducts) {
            assertTrue(allProduct.getName().concat(allProduct.getDescription()).contains(keyWord));
        }
    }

    @Test
    public void givenKeyWordNonExistingInDB_whenSearchReturnsEmptyResults_thenCorrect() {
        String keyWord = "@##@#";
        List<Product> allProducts = productDao.searchProducts(keyWord);
        assertTrue(allProducts.isEmpty());
    }



    @Test
    public void givenExistingId_whenReturned_thenCorrect() {
        int productId = 24;
        Product product = productDao.getOneById(productId);
        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("It : film tie-in edition of Stephen...", product.getName());
        assertEquals("Stephen King. It : film tie-in edition of Stephen...", product.getDescription());
        assertEquals(0, product.getPrice().compareTo(new BigDecimal(324)));
        assertFalse(product.getPictureUrl().isEmpty());
    }

    @Test
    public void givenNonExistingId_whenNullReturned_thenCorrect() {
        int productId = 2228;
        assertNull(productDao.getOneById(productId));
    }

    @Test
    public void givenExistingIdAndGetProductChangeFieldsAndUpdate_whenGetByIdReturnsUpdated_thenCorrect() {
        int productId = 28;
        Product oldProduct = productDao.getOneById(productId);
        String oldProductVersion = oldProduct.getVersionId();
        String newDescription = oldProduct.getDescription() + ". New Edition";
        oldProduct.setDescription(newDescription);
        BigDecimal newPrice = oldProduct.getPrice().add(new BigDecimal(100));
        oldProduct.setPrice(newPrice);
        Product updatedProduct = productDao.updateOne(oldProduct);
        Product product = productDao.getOneById(productId);
        assertNotNull(product);
        assertNotEquals(oldProductVersion, product.getVersionId());
        assertEquals(productId, product.getId());
        assertEquals("The Bazaar of Bad Dreams Export", product.getName());
        assertEquals(newDescription, product.getDescription());
        assertEquals(newPrice, product.getPrice());
        assertFalse(product.getPictureUrl().isEmpty());
        assertEquals(updatedProduct, product);
    }

    @Test
    public void givenExistingIdAndGetProductAndChangeAndUpdateButTheProductWasAlreadyUpdatedInParallel_whenGetByIdReturnsNonUpdated_thenCorrect() {
        int productId = 28;
        Product productVersion0 = productDao.getOneById(productId);
        String version0 = productVersion0.getVersionId();
        String newDescription = productVersion0.getDescription() + ". New Edition";
        productVersion0.setDescription(newDescription);
        BigDecimal newPrice = productVersion0.getPrice().add(new BigDecimal(100));
        productVersion0.setPrice(newPrice);
        Product productVersion0UpdatedToVersion1 = productDao.updateOne(productVersion0);
        Product productVersion1 = productDao.getOneById(productId);
        assertNotNull(productVersion1);
        assertNotEquals(version0, productVersion1.getVersionId());
        assertEquals(productId, productVersion1.getId());
        assertEquals("The Bazaar of Bad Dreams Export", productVersion1.getName());
        assertEquals(newDescription, productVersion1.getDescription());
        assertEquals(newPrice, productVersion1.getPrice());
        assertFalse(productVersion1.getPictureUrl().isEmpty());
        assertEquals(productVersion0UpdatedToVersion1, productVersion1);

        //Imitate kinda the product was updated in parallel session
        String product1Version = productVersion1.getVersionId();
        productVersion1.setVersionId(UUID.randomUUID().toString());
        productVersion1.setDescription(productVersion1.getDescription()+"!!!");
        Product productVersion1NotUpdatedToVersion2 = productDao.updateOne(productVersion1);
        assertEquals(productVersion1.getVersionId(), productVersion1NotUpdatedToVersion2.getVersionId());
        assertNotEquals(product1Version, productVersion1NotUpdatedToVersion2.getVersionId());
        assertEquals(productVersion1.getDescription(), productVersion1NotUpdatedToVersion2.getDescription());
    }

    @Test
    public void givenNonExistingIdAndGetProductChangeFieldsAndUpdate_whenGetByIdReturnsUpdated_thenCorrect() {
        int productId = 288;
        String productName = "Non Existing Product";
        String productDescription = "Non Existing Product Description";
        BigDecimal newPrice = new BigDecimal(105);
        Product oldProduct = new Product(productId, productName, productDescription, "", newPrice);
        Product updatedProduct = productDao.updateOne(oldProduct);
        Product product = productDao.getOneById(productId);
        assertEquals(oldProduct, updatedProduct);
        assertNull(product);
    }

    @Test
    public void givenNewProductThenInsert_whenGetByIdReturnsNewProduct_thenCorrect() {
        int productId = 2222;
        String productName = "New Book";
        String productDescription = "New Book Description";
        BigDecimal productPrice = new BigDecimal(2314);
        String pictureUrl = "http://google.com//picturetest.jpg";
        Product newProduct = new Product(productId, productName, productDescription, pictureUrl, productPrice);
        Product insertedProduct = productDao.insertOne(newProduct);

        Product product = productDao.getOneById(insertedProduct.getId());
        assertNotNull(product);
        assertEquals(insertedProduct.getId(), product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(0, product.getPrice().compareTo(productPrice));
        assertEquals(pictureUrl, product.getPictureUrl());
        assertEquals(insertedProduct, product);
    }
}