package edu.ted.webshop.web.dto;

import edu.ted.webshop.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductConverterTest {

    @Test
    void givenProductDTOAndConvertToProduct_whenProductPropertiesCoincide_thenCorrect() {
        final String productId = "222";
        final String productName = "Name";
        final String productDescription = "Description";
        final String productPictureUrl = "http://127.0.0.1/picture.jpg";
        final String productPrice = "5566.5";
        final String productVersionId = "5566-ddcc-bbnn";

        ProductDTO productDto = new ProductDTO(productId, productName, productDescription, productPictureUrl, productPrice, productVersionId);
        Product product = ProductConverter.toProduct(productDto);
        assertEquals(Integer.parseInt(productId), product.getId());
        assertEquals(productName, product.getName());
        assertEquals(productDescription, product.getDescription());
        assertEquals(productPictureUrl, product.getPictureUrl());
        assertEquals(new BigDecimal(productPrice), product.getPrice());
        assertEquals(productVersionId, product.getVersionId());
    }

    @Test
    void givenProductAndConvertToProductDTO_whenProductPropertiesCoincide_thenCorrect() {
        final int productId = 222;
        final String productName = "Name";
        final String productDescription = "Description";
        final String productPictureUrl = "http://127.0.0.1/picture.jpg";
        final BigDecimal productPrice = new BigDecimal("5566.4");
        final String productVersionId = "5566-ddcc-bbnn";

        Product product = new Product(productId, productName, productDescription, productPictureUrl, productPrice);
        product.setVersionId(productVersionId);
        ProductDTO productDto = ProductConverter.fromProduct(product);
        assertEquals(productDto.getId(), Integer.toString(product.getId()));
        assertEquals(productDto.getName(), product.getName());
        assertEquals(productDto.getDescription(), product.getDescription());
        assertEquals(productDto.getPictureUrl(), product.getPictureUrl());
        assertEquals(productDto.getPrice(), product.getPrice().toString());
        assertEquals(productDto.getVersionId(), product.getVersionId());
    }
}