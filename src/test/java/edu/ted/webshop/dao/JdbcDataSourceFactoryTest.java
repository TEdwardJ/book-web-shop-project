package edu.ted.webshop.dao;

import edu.ted.webshop.utils.PropertyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class JdbcDataSourceFactoryTest {

    private DataSourceFactory factory;

    private Properties dataSourceProperties;

    @BeforeEach
    public void init() {
        dataSourceProperties = PropertyReader.readPropertyFile("db.properties");
        factory = new JdbcDataSourceFactory();
        ((JdbcDataSourceFactory) factory).setDataSourceProperties(dataSourceProperties);
    }

    @Test

    public void givenDataSourceFactory_whenGetPropertiesAndItSet_thenCorrect(){
        assertEquals(dataSourceProperties, ((JdbcDataSourceFactory) factory).getDataSourceProperties());
    }

    @Test
    void givenDataSourceFactory_thenGetConnection_whenNoException_thenCorrect() throws SQLException {
        final DataSource dataSource = factory.getDataSource();
        Connection connection = dataSource.getConnection();
        connection.close();
    }
}