package edu.ted.webshop.servlets;

import edu.ted.webshop.controller.ProductController;
import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.dao.ShopDataSourceFactory;
import edu.ted.webshop.utils.PropertyReader;
import edu.ted.webshop.utils.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Properties;

public class ConfigContextListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();
        final TemplateEngine templateEngine = new TemplateEngine();
        servletContext.setAttribute("templateEngine", templateEngine);
        Properties dataSourceProperties = PropertyReader.readPropertyFile("db.properties");
        logger.info("dsProperties entries: {}", dataSourceProperties.size());
        Properties queries = PropertyReader.readPropertyFile("query.properties");
        logger.info("Query entries: {}", queries.size());
        final ShopDataSourceFactory dataSourceFactory = new ShopDataSourceFactory();
        dataSourceFactory.setDataSourceProperties(dataSourceProperties);
        final JdbcProductDao productDao = new JdbcProductDao(dataSourceFactory, templateEngine);
        productDao.setQueries(queries);

        servletContext.setAttribute("productDAO", productDao);
        servletContext.setAttribute("productController", new ProductController(productDao));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
