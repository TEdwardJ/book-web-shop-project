package edu.ted.webshop.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class JdbcDataSourceFactory implements DataSourceFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Properties dataSourceProperties;

    public void setDataSourceProperties(Properties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    public Properties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setUrl(dataSourceProperties.getProperty("db.url"));
        final String driverName = dataSourceProperties.getProperty("db.driverClassName");
        final String user = dataSourceProperties.getProperty("db.user");
        dataSource.setDriverClassName(driverName);
        dataSource.setUsername(user);
        dataSource.setPassword(dataSourceProperties.getProperty("db.password"));
        logger.info("Factory returns source: user {}, driver {}", user, driverName);
        return dataSource;
}

}
