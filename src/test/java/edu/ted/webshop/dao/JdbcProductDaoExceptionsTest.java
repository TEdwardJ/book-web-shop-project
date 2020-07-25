package edu.ted.webshop.dao;

import edu.ted.webshop.entity.Product;
import edu.ted.webshop.exception.DataException;
import edu.ted.webshop.utils.PropertyReader;
import edu.ted.webshop.utils.TemplateEngine;
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

    @InjectMocks
    private static JdbcProductDao productDao;

    @Mock
    private DataSource dataSource;

    @BeforeAll
    public static void init() {
        Properties dataSourceProperties = PropertyReader.readPropertyFile("db.properties");
        final JdbcDataSourceFactory dataSourceFactory = new JdbcDataSourceFactory(dataSourceProperties);
        productDao = new JdbcProductDao(dataSourceFactory.getDataSource(), new TemplateEngine("/product/"));
        Properties queries = PropertyReader.readPropertyFile("query.properties");
        productDao.setQueries(queries);
    }

    @BeforeEach
    public void initEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetAllAndSomeException_whenDataException_thenCorrect() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        Throwable thrown = assertThrows(DataException.class, ()->productDao.getAll());
        assertEquals("Attempt to get all products from DB failed", thrown.getMessage());
    }

    @Test
    public void whenSearchProductsAndSomeException_whenDataException_thenCorrect() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        Throwable thrown = assertThrows(DataException.class, ()->productDao.searchProducts("keyWord"));
        assertEquals("Attempt to search product in DB failed", thrown.getMessage());
    }

    @Test
    public void whenGetOneByIdAndSomeException_whenDataException_thenCorrect() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        Throwable thrown = assertThrows(DataException.class, ()->productDao.getOneById(28));
        assertEquals("Attempt to get one product by Id from DB failed", thrown.getMessage());
    }

    @Test
    public void whenUpdateOneAndSomeException_whenDataException_thenCorrect() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        Throwable thrown = assertThrows(DataException.class, ()->productDao.updateOne(new Product()));
        assertEquals("Attempt to update one product in DB failed", thrown.getMessage());
    }

    @Test
    public void whenInsertOneAndSomeException_whenDataException_thenCorrect() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        Throwable thrown = assertThrows(DataException.class, ()->productDao.insertOne(new Product()));
        assertEquals("Attempt to add one product into DB failed", thrown.getMessage());
    }

}