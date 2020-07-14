package edu.ted.webshop.dao;

import javax.sql.DataSource;

public interface DataSourceFactory {

    DataSource getDataSource();
}
