package edu.ted.webshop.utils;

import edu.ted.webshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProductRowMapperTest {

    @Mock
    private ResultSet resultSet;

    private final ProductRowMapper rowMapper = new ProductRowMapper();

    private Product product;

    @BeforeEach
    public void init(){
        product = new Product(3232, "productName", "productDescription", "", new BigDecimal(568));
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void map() throws SQLException {
        when(resultSet.getInt(1)).thenReturn(product.getId());
        when(resultSet.getString(2)).thenReturn(product.getName());
        when(resultSet.getString(3)).thenReturn(product.getDescription());
        when(resultSet.getString(4)).thenReturn(product.getPictureUrl());
        when(resultSet.getBigDecimal(5)).thenReturn(product.getPrice());
        Product returnedProduct = rowMapper.map(resultSet);
        assertEquals(product, returnedProduct);
    }
}