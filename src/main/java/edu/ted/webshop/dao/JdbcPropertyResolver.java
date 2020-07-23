package edu.ted.webshop.dao;

import edu.ted.webshop.utils.ProductRowMapper;
import edu.ted.webshop.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class JdbcPropertyResolver {

    private static Logger LOGGER = LoggerFactory.getLogger(ProductRowMapper.class);

    public static Properties resolve() {
        String dbUriString = System.getenv("DATABASE_URL");
        Properties dataSourceProperties = new Properties();
        //Heroku case
        if (dbUriString != null) {
            LOGGER.info("Heroku DBUrlString: {}", dbUriString);
            try {
                URI dbUri = new URI(dbUriString);
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                LOGGER.info("Heroku dbUrl: {}", dbUrl);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];

                LOGGER.info("Heroku DB user: {}", username);

                dataSourceProperties.setProperty("db.url", dbUrl);
                dataSourceProperties.setProperty("db.driverCdb.lassName", "org.postgresql.Driver");
                dataSourceProperties.setProperty("db.user", username);
                dataSourceProperties.setProperty("db.password", password);
            } catch (URISyntaxException e) {
                LOGGER.error("Configuration of DataSource Failed:", e);
            }
        } else {
            dataSourceProperties = PropertyReader.readPropertyFile("db.properties");
        }
        return dataSourceProperties;
    }
}
