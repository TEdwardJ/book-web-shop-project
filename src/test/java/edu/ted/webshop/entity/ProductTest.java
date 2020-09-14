package edu.ted.webshop.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void givenProductFromDefaultConstructorAndSetProperties_whenGettersReturnTheSameValues_thenCorrect() {
        int productId = 222;
        String productName = "Name";
        String productDescription = "Description";
        String productPictureUrl = "http://127.0.0.1/picture.jpg";
        String productPrice = "5566.5";
        String productVersionId = "5566-ddcc-bbnn";
        Product product = new Product();
        product.setId(productId);
        product.setName(productName);
        product.setDescription(productDescription);
        product.setPictureUrl(productPictureUrl);
        product.setPrice(new BigDecimal(productPrice));
        product.setVersionId(productVersionId);
        assertEquals(productId, product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(new BigDecimal(productPrice), product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }

    @Test
    void given2ProductsWithTheSameFields_whenEqual_thenCorrect() {
        String productName = "Name";
        String productDescription = "Description";
        String productPictureUrl = "http://127.0.0.1/picture.jpg";
        String productPrice = "5566.5";
        String productVersionId = "5566-ddcc-bbnn";
        Product product1 = new Product(productName, productDescription, productPictureUrl, new BigDecimal(productPrice));
        Product product2 = new Product(productName, productDescription, productPictureUrl, new BigDecimal(productPrice));
        product1.setVersionId(productVersionId);
        assertEquals(product2, product1);
        assertEquals(Objects.hashCode(product1), Objects.hashCode(product2));
    }

    @Test
    void givenProductWithoutId_whenGettersReturnTheSameValues_thenCorrect() {
        String productName = "Name";
        String productDescription = "Description";
        String productPictureUrl = "http://127.0.0.1/picture.jpg";
        String productPrice = "5566.5";
        String productVersionId = "5566-ddcc-bbnn";
        Product product = new Product(productName, productDescription, productPictureUrl, new BigDecimal(productPrice));
        product.setVersionId(productVersionId);
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(new BigDecimal(productPrice), product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }

    @Test
    void givenProductWithId_whenGettersReturnTheSameValues_thenCorrect() {
        int productId = 222;
        String productName = "Name";
        String productDescription = "Description";
        String productPictureUrl = "http://127.0.0.1/picture.jpg";
        String productPrice = "5566.5";
        String productVersionId = "5566-ddcc-bbnn";
        Product product = new Product(productId, productName, productDescription, productPictureUrl, new BigDecimal(productPrice));
        product.setVersionId(productVersionId);
        assertEquals(productId, product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(new BigDecimal(productPrice), product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }
}