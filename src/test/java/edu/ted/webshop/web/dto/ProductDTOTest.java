package edu.ted.webshop.web.dto;

import edu.ted.webshop.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
    void givenProductDTOFromConstructorWithVersion_whenGettersReturnGivenValues_thenCorrect() {
        final String productId = "222";
        final String productName = "Name";
        final String productDescription = "Description";
        final String productPictureUrl = "http://127.0.0.1/picture.jpg";
        final String productPrice = "5566";
        final String productVersionId = "5566-ddcc-bbnn";

        ProductDTO product = new ProductDTO(productId, productName, productDescription, productPictureUrl, productPrice, productVersionId);

        assertEquals(productId, product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(productPrice, product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }

    @Test
    void givenProductDTOAndGenerateProduct_whenProductPropertiesCoincide_thenCorrect() {
        final String productId = "222";
        final String productName = "Name";
        final String productDescription = "Description";
        final String productPictureUrl = "http://127.0.0.1/picture.jpg";
        final String productPrice = "5566";
        final String productVersionId = "5566-ddcc-bbnn";

        ProductDTO productDto = new ProductDTO(productId, productName, productDescription, productPictureUrl, productPrice, productVersionId);
        Product product = productDto.getProduct();
        assertEquals(Integer.parseInt(productId), product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(new BigDecimal(productPrice), product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }


}