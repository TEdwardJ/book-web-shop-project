package edu.ted.webshop.dao;

import edu.ted.webshop.utils.PropertyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ShopDataSourceFactoryTest {

    private DataSourceFactory factory;

    private Properties dataSourceProperties;

    @BeforeEach
    public void init() {
        dataSourceProperties = PropertyReader.readPropertyFile("db.properties");
        factory = new ShopDataSourceFactory();
        ((ShopDataSourceFactory) factory).setDataSourceProperties(dataSourceProperties);
    }

    @Test

    public void givenDataSourceFactory_whenGetPropertiesAndItSet_thenCorrect(){
        assertEquals(dataSourceProperties, ((ShopDataSourceFactory) factory).getDataSourceProperties());
    }

    @Test
    void givenDataSourceFactory_thenGetConnection_whenNoException_thenCorrect() throws SQLException {
        final DataSource dataSource = factory.getDataSource();
        Connection connection = dataSource.getConnection();
        connection.close();
    }
}