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
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl !=null){
            try {
                URI dbUri = new URI(dbUrl);
                dataSource.setServerNames(new String[]{dbUri.getHost()});
                dataSource.setDatabaseName(dbUri.getPath().substring(1));
                dataSource.setPortNumbers(new int[]{dbUri.getPort()});
                String userInfo = dbUri.getUserInfo();
                dataSource.setUser(userInfo.substring(0,userInfo.indexOf(":")));
                dataSource.setPassword(userInfo.substring(userInfo.indexOf(":")+1));
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
