package edu.ted.webshop.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    @Test
    void givenProductDTOFromConstructorWithoutVersion_whenGettersReturnGivenValues_thenCorrect() {
        final String productId = "222";
        final String productName = "Name";
        final String productDescription = "Description";
        final String productPictureUrl = "http://127.0.0.1/picture.jpg";
        final String productPrice = "5566";
        final String productVersionId = "5566-ddcc-bbnn";

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
        final String productId = "0";
        final String productName = "";
        final String productDescription = "";
        final String productPictureUrl = "";
        final String productPrice = "";
        final String productVersionId = "";

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
        final String productId = "222";
        final String productName = "Name";
        final String productDescription = "Description";
        final String productPictureUrl = "http://127.0.0.1/picture.jpg";
        final String productPrice = "5566";
        final String productVersionId = "5566-ddcc-bbnn";

        ProductDTO product = new ProductDTO();

        assertEquals(productId, product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(productPrice, product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }

}