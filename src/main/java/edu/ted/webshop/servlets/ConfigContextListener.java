package edu.ted.webshop.servlets;

import edu.ted.webshop.controller.ProductController;
import edu.ted.webshop.dao.JdbcProductDao;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConfigContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();
        final JdbcProductDao productDao = new JdbcProductDao();
        servletContext.setAttribute("productDAO", productDao);
        servletContext.setAttribute("productController", new ProductController(productDao));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
