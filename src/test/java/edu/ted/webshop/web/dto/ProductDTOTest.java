package edu.ted.webshop.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    @Test
    void givenProductDTOFromConstructorWithoutVersion_whenGettersReturnGivenValues_thenCorrect() {
        String productId = "222";
        String productName = "Name";
        String productDescription = "Description";
        String productPictureUrl = "http://127.0.0.1/picture.jpg";
        String productPrice = "5566";
        String productVersionId = "5566-ddcc-bbnn";

        ProductDTO product = new ProductDTO(productId, productName, productDescription, productPictureUrl, productPrice);
        product.setVersionId(productVersionId);

        assertEquals(productId, product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(productPrice, product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }

    @Test
    void givenProductDTOFromConstructorWithNoParameters_whenGettersReturnEmptyValues_thenCorrect() {
        String productId = "0";
        String productName = "";
        String productDescription = "";
        String productPictureUrl = "";
        String productPrice = "";
        String productVersionId = "";

        ProductDTO product = new ProductDTO();

        assertEquals(productId, product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(productPrice, product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }

    @Test
    void givenProductDTOFromConstructorWithVersion_whenGettersReturnGivenValues_thenCorrect() {
        String productId = "222";
        String productName = "Name";
        String productDescription = "Description";
        String productPictureUrl = "http://127.0.0.1/picture.jpg";
        String productPrice = "5566";
        String productVersionId = "5566-ddcc-bbnn";

        ProductDTO product = new ProductDTO(productId, productName, productDescription, productPictureUrl, productPrice, productVersionId);

        assertEquals(productId, product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(productPrice, product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }

}