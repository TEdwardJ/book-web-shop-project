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
        String dbUriString = System.getenv("DATABASE_URL");
        logger.info("Heroku DBUrl: {}", dbUriString);
        if (dbUriString != null) {
            try {
                URI dbUri = new URI(dbUriString);
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                logger.info("Heroku dbUrl: {}", dbUrl);


                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];

                dataSource.setUser(username);
                logger.info("Heroku DB user: {}", username);
                dataSource.setPassword(password);
            } catch (URISyntaxException e) {
                logger.error("Configuration of DataSource Failed: {}", e);
            }
        } else {

            dataSource.setServerNames(new String[]{dataSourceProperties.getProperty("db.serverName")});
            dataSource.setPortNumbers(new int[]{Integer.parseInt(dataSourceProperties.getProperty("db.port"))});
            dataSource.setUser(dataSourceProperties.getProperty("db.user"));
            dataSource.setPassword(dataSourceProperties.getProperty("db.password"));
        }
        return dataSource;
    }

}
