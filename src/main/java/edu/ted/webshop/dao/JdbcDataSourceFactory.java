package edu.ted.webshop.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class JdbcDataSourceFactory implements DataSourceFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Properties dataSourceProperties;

    private final HikariConfig config;
    private static HikariDataSource hikariDataSource;

    public JdbcDataSourceFactory(Properties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
        config = new HikariConfig(dataSourceProperties);

    }

    public DataSource getDataSource() {
        final String user = dataSourceProperties.getProperty("db.user");
        //config.setMaximumPoolSize();
        hikariDataSource = new HikariDataSource(config);
        hikariDataSource.setMaximumPoolSize(4);

        logger.info("Factory returns Hikari DataSource: user {}, driver {}", user, hikariDataSource.getDataSourceClassName());
        return hikariDataSource;//dataSource;
    }

}
