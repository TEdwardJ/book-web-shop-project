package edu.ted.webshop.dao;

import edu.ted.webshop.entity.Product;
import edu.ted.webshop.exception.DataException;
import edu.ted.webshop.dao.mapper.ProductRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class JdbcProductDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DataSource dataSource;

    public JdbcProductDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Product> getAll() {
        List<Product> productsList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            final String selectAllQuery = ProductCRUDGenerator.getSelectAllQuery();
            boolean executed = statement.execute(selectAllQuery);
            logger.debug("Prepared Query: {}", selectAllQuery);
            if (executed) {
                ResultSet results = statement.getResultSet();
                while (results.next()) {
                    productsList.add(ProductRowMapper.map(results));
                }
            }
            return productsList;
        } catch (SQLException throwable) {
            logger.error("DB Error occurred:", throwable);
            throw new DataException("Attempt to get all products from DB failed", throwable);
        }
    }

    public List<Product> searchProducts(String keyWord) {
        List<Product> productsList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String query = ProductCRUDGenerator.getFindAllQuery(keyWord);
            logger.debug("Prepared Query: {}", query);
            boolean executed = statement.execute(query);
            if (executed) {
                ResultSet results = statement.getResultSet();
                while (results.next()) {
                    productsList.add(ProductRowMapper.map(results));
                }
            }
            return productsList;
        } catch (SQLException throwable) {
            logger.error("DB Error occurred:", throwable);
            throw new DataException("Attempt to search product in DB failed", throwable);
        }
    }


    public Product getOneById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String query = ProductCRUDGenerator.getGetOneQuery(id);
            logger.debug("Prepared Query: {}", query);
            boolean executed = statement.execute(query);
            if (executed) {
                ResultSet results = statement.getResultSet();
                if (results.next()) {
                    return ProductRowMapper.map(results);
                }
            }
            return null;
        } catch (SQLException throwable) {
            logger.error("DB Error occurred:", throwable);
            throw new DataException("Attempt to get one product by Id from DB failed", throwable);
        }
    }

    public Product updateOne(Product product) {
        String oldVersionId = Optional.ofNullable(product.getVersionId()).orElse("");
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            product.setVersionId(UUID.randomUUID().toString());
            String query = ProductCRUDGenerator.getUpdateQuery(product, oldVersionId);
            logger.debug("Prepared Query: {}", query);
            statement.execute(query);
            int updatedCount = statement.getUpdateCount();
            if (updatedCount == 0) {
                product.setVersionId(oldVersionId);
            }
            return product;
        } catch (SQLException throwable) {
            logger.error("DB Error occurred:", throwable);
            product.setVersionId(oldVersionId);
            throw new DataException("Attempt to update one product in DB failed", throwable);
        }
    }

    public Product insertOne(Product product) {
        int newProductId = 0;
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            product.setVersionId(UUID.randomUUID().toString());
            String query = ProductCRUDGenerator.getInsertQuery(product);//getPreparedQuery("insertNew", parametersMap);
            logger.debug("Prepared Query: {}", query);
            statement.execute(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                newProductId = result.getInt(1);
            }
        } catch (SQLException throwable) {
            logger.error("DB Error occurred:", throwable);
            throw new DataException("Attempt to add one product into DB failed", throwable);
        }
        return getOneById(newProductId);
    }
}
