package edu.ted.webshop.dao;

import edu.ted.webshop.entity.Product;
import edu.ted.webshop.exception.DataException;
import edu.ted.webshop.utils.PropertyReader;
import edu.ted.webshop.utils.FreeMarkerTemplateEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JdbcProductDaoExceptionsTest {

    private JdbcProductDao productDao;

    @Mock
    private DataSource dataSource;

    @BeforeEach
    public void init() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dataSource.getConnection()).thenThrow(new SQLException());
        productDao = new JdbcProductDao(dataSource);
    }

    @Test
    public void whenGetAllAndSomeException_whenDataException_thenCorrect() {
        Throwable thrown = assertThrows(DataException.class, ()->productDao.getAll());
        assertEquals("Attempt to get all products from DB failed", thrown.getMessage());
    }

    @Test
    public void whenSearchProductsAndSomeException_whenDataException_thenCorrect() {
        Throwable thrown = assertThrows(DataException.class, ()->productDao.searchProducts("keyWord"));
        assertEquals("Attempt to search product in DB failed", thrown.getMessage());
    }

    @Test
    public void whenGetOneByIdAndSomeException_whenDataException_thenCorrect() {
        Throwable thrown = assertThrows(DataException.class, ()->productDao.getOneById(28));
        assertEquals("Attempt to get one product by Id from DB failed", thrown.getMessage());
    }

    @Test
    public void whenUpdateOneAndSomeException_whenDataException_thenCorrect() {
        Throwable thrown = assertThrows(DataException.class, ()->productDao.updateOne(new Product()));
        assertEquals("Attempt to update one product in DB failed", thrown.getMessage());
    }

    @Test
    public void whenInsertOneAndSomeException_whenDataException_thenCorrect() {
        Throwable thrown = assertThrows(DataException.class, ()->productDao.insertOne(new Product()));
        assertEquals("Attempt to add one product into DB failed", thrown.getMessage());
    }

}