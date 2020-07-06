package edu.ted.webshop.dao;

import edu.ted.webshop.utils.PropertyReader;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class PostgresDataSourceFactory {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Properties dataSourceProperties;

    public PostgresDataSourceFactory(String propertiesFile) throws IOException {
        dataSourceProperties = PropertyReader.readPropertyFile(propertiesFile);
    }

    public DataSource getDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        logger.info("Heroku DBUrl: {}", dbUrl);
        if (dbUrl != null) {
            try {
                URI dbUri = new URI(dbUrl);
                dataSource.setServerNames(new String[]{dbUri.getHost()});
                logger.info("Heroku DB Host: {}", dbUri.getHost());
                dataSource.setDatabaseName(dbUri.getPath().substring(1));
                logger.info("Heroku DB Name: {}", dbUri.getPath().substring(1));
                dataSource.setPortNumbers(new int[]{dbUri.getPort()});
                logger.info("Heroku DB Port: {}", dbUri.getPort());
                String userInfo = dbUri.getUserInfo();
                dataSource.setUser(userInfo.substring(0, userInfo.indexOf(":")));
                logger.info("Heroku DB user: {}", userInfo.indexOf(":"));
                dataSource.setPassword(userInfo.substring(userInfo.indexOf(":") + 1));
                return dataSource;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        dataSource.setServerNames(new String[]{dataSourceProperties.getProperty("db.serverName")});
        dataSource.setPortNumbers(new int[]{Integer.parseInt(dataSourceProperties.getProperty("db.port"))});
        dataSource.setUser(dataSourceProperties.getProperty("db.user"));
        dataSource.setPassword(dataSourceProperties.getProperty("db.password"));
        return dataSource;
    }

}
