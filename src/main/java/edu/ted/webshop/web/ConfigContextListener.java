package edu.ted.webshop.web;

import edu.ted.webshop.dao.JdbcDataSourceFactory;
import edu.ted.webshop.dao.JdbcPropertyResolver;
import edu.ted.webshop.service.ProductService;
import edu.ted.webshop.dao.JdbcProductDao;
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
        final TemplateEngine templateEngine = new TemplateEngine("/product/");
        templateEngine.init();
        servletContext.setAttribute("templateEngine", templateEngine);
        final Properties queries = PropertyReader.readPropertyFile("query.properties");
        logger.info("Query entries: {}", queries.size());
        final Properties dataSourceProperties = JdbcPropertyResolver.resolve();
        logger.info("dsProperties entries: {}", dataSourceProperties.size());
        final JdbcDataSourceFactory dataSourceFactory = new JdbcDataSourceFactory(dataSourceProperties);
        final JdbcProductDao productDao = new JdbcProductDao(dataSourceFactory.getDataSource(), templateEngine);
        productDao.setQueries(queries);

        servletContext.setAttribute("productDAO", productDao);
        servletContext.setAttribute("productController", new ProductService(productDao));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
