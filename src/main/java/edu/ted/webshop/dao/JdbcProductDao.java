package edu.ted.webshop.dao;

import edu.ted.webshop.entity.Product;
import edu.ted.webshop.exception.DataException;
import edu.ted.webshop.utils.ProductRowMapper;
import edu.ted.webshop.utils.TemplateEngine;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class JdbcProductDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DataSourceFactory dataSourceFactory;

    private final TemplateEngine templateEngine;

    private DataSource dataSource;
    private Properties queries;

    public JdbcProductDao(DataSourceFactory dataSourceFactory, TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.dataSourceFactory = dataSourceFactory;
        dataSource = dataSourceFactory.getDataSource();
    }

    public void setQueries(Properties queries) {
        this.queries = queries;
    }

    public List<Product> getAll() {
        List<Product> productsList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {

            Statement statement = connection.createStatement();
            boolean executed = statement.execute(queries.getProperty("selectAll"));
            logger.debug("Prepared Query: {}", queries.getProperty("selectAll"));
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                productsList.add(ProductRowMapper.map(results));
            }
            return productsList;
        } catch (SQLException throwables) {
            logger.error("DB Error occurred:", throwables);
            throw new DataException("Attempt to get all products from DB failed", throwables);
        }
    }

    public List<Product> searchProducts(String keyWord) {
        List<Product> productsList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Map<String, Object> parametersMap = new HashMap<>();
            parametersMap.put("keyWord", keyWord);
            Statement statement = connection.createStatement();
            String query = getPreparedQuery("findAll", parametersMap);
            logger.debug("Prepared Query: {}", query);
            boolean executed = statement.execute(query);
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                productsList.add(ProductRowMapper.map(results));
            }
            return productsList;
        } catch (SQLException throwables) {
            logger.error("DB Error occurred:", throwables);
            throw new DataException("Attempt to search product in DB failed", throwables);
        } catch (TemplateException | IOException e) {
            logger.error("Query preparation error occurred. See log above");
            throw new DataException(e);
        }
    }

    String getPreparedQuery(String queryName, Map<String, Object> parametersMap) throws IOException, TemplateException {
        String query = queries.getProperty(queryName);
        StringWriter writer = new StringWriter();

        templateEngine.writeString(queryName, query, writer, parametersMap);
        return writer.toString();
    }

    public Product getOneById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            Map<String, Object> parametersMap = new HashMap<>();
            parametersMap.put("productId", id);
            String query = getPreparedQuery("getOne", parametersMap);
            logger.debug("Prepared Query: {}", query);
            boolean executed = statement.execute(query);
            ResultSet results = statement.getResultSet();
            if (results.next()) {
                Product product = ProductRowMapper.map(results);
                return product;
            }
            return null;
        } catch (SQLException throwables) {
            logger.error("DB Error occurred:", throwables);
            throw new DataException("Attempt to get one product by Id from DB failed", throwables);
        } catch (TemplateException | IOException e) {
            logger.error("Query preparation error occurred. See log above");
            throw new DataException(e);
        }
    }

    public Product updateOne(Product product) {
        String oldVersionId = Optional.ofNullable(product.getVersionId()).orElse("");
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            Map<String, Object> parametersMap = new HashMap<>();
            product.setVersionId(UUID.randomUUID().toString());
            parametersMap.put("product", product);
            parametersMap.put("versionId", oldVersionId);
            String query = getPreparedQuery("updateOne", parametersMap);
            logger.debug("Prepared Query: {}", query);
            boolean executed = statement.execute(query);
            final int updatedCount = statement.getUpdateCount();
            if (updatedCount == 0) {
                product.setVersionId(oldVersionId);
            }
            return product;
        } catch (SQLException throwables) {
            logger.error("DB Error occurred:", throwables);
            product.setVersionId(oldVersionId);
            throw new DataException("Attempt to update one product in DB failed", throwables);
        } catch (TemplateException | IOException e) {
            logger.error("Query preparation error occurred. See log above");
            product.setVersionId(oldVersionId);
            throw new DataException(e);
        }
    }

    public Product insertOne(Product product) {
        int newProductId = 0;
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            Map<String, Object> parametersMap = new HashMap<>();
            product.setVersionId(UUID.randomUUID().toString());
            parametersMap.put("product", product);
            String query = getPreparedQuery("insertNew", parametersMap);
            logger.debug("Prepared Query: {}", query);
            statement.execute(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                newProductId = result.getInt(1);
            }
        } catch (SQLException throwables) {
            logger.error("DB Error occurred:", throwables);
            throw new DataException("Attempt to add one product into DB failed", throwables);
        } catch (TemplateException | IOException e) {
            logger.error("Query preparation error occurred. See log above");
            throw new DataException(e);
        }
        return getOneById(newProductId);
    }

}
