package edu.ted.webshop.servlets;

import edu.ted.webshop.controller.ProductController;
import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.utils.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConfigContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();
        final TemplateEngine templateEngine = new TemplateEngine();
        servletContext.setAttribute("templateEngine", templateEngine);
        final JdbcProductDao productDao = new JdbcProductDao(templateEngine);
        servletContext.setAttribute("productDAO", productDao);
        servletContext.setAttribute("productController", new ProductController(productDao));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
