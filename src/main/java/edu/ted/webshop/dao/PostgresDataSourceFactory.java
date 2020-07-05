package edu.ted.webshop.dao;

import edu.ted.webshop.utils.PropertyReader;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class PostgresDataSourceFactory {
    private Properties dataSourceProperties;

    public PostgresDataSourceFactory(String propertiesFile) throws IOException {
        dataSourceProperties = PropertyReader.readPropertyFile(propertiesFile);
    }

    public DataSource getDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setServerNames(new String[]{dataSourceProperties.getProperty("db.serverName")});
        dataSource.setPortNumbers(new int[]{Integer.parseInt(dataSourceProperties.getProperty("db.port"))});
        dataSource.setUser(dataSourceProperties.getProperty("db.user"));
        dataSource.setPassword(dataSourceProperties.getProperty("db.password"));
        return dataSource;
    }

}
