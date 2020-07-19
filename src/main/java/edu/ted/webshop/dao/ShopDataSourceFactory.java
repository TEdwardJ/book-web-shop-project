package edu.ted.webshop.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class ShopDataSourceFactory implements DataSourceFactory {

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
        String dbUriString = System.getenv("DATABASE_URL");
        //Heroku case
        if (dbUriString != null) {
            logger.info("Heroku DBUrlString: {}", dbUriString);
            try {
                URI dbUri = new URI(dbUriString);
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                logger.info("Heroku dbUrl: {}", dbUrl);

                dataSource.setUrl(dbUrl);
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];

                dataSource.setUsername(username);
                logger.info("Heroku DB user: {}", username);
                dataSource.setPassword(password);
            } catch (URISyntaxException e) {
                logger.error("Configuration of DataSource Failed: {}", e);
            }
            //Local cases - real DB and test DB
        } else {
            dataSource.setUrl(dataSourceProperties.getProperty("db.url"));
            dataSource.setDriverClassName(dataSourceProperties.getProperty("db.driverClassName"));
            dataSource.setUsername(dataSourceProperties.getProperty("db.user"));
            dataSource.setPassword(dataSourceProperties.getProperty("db.password"));
        }
        return dataSource;
    }

}
