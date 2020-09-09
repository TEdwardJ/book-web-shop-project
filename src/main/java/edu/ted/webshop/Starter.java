package edu.ted.webshop;

import edu.ted.templator.TemplateProcessor;
import edu.ted.webshop.dao.JdbcDataSourceFactory;
import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.dao.JdbcPropertyResolver;
import edu.ted.webshop.service.ProductService;
import edu.ted.webshop.dao.mapper.ProductRowMapper;
import edu.ted.webshop.utils.PropertyReader;
import edu.ted.webshop.utils.FreeMarkerTemplateEngine;
import edu.ted.webshop.utils.TemplatorEngine;
import edu.ted.webshop.utils.interfaces.TemplateEngine;
import edu.ted.webshop.web.filter.LoggingFilter;
import edu.ted.webshop.web.servlet.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Properties;

public class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRowMapper.class);

    private static Server server;
    private static TemplateEngine templatorEngine;

    public static Server getServer() {
        return server;
    }


    public static void main(String[] args) throws Exception {
        server = new Server();
        init();
        server.start();
        LOGGER.info("Server started");
    }

    public static void init() {
        System.out.println(System.getenv("PORT") + " is the port");

        int port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8081"));

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});

        ServletContextHandler mainContextHandler = new ServletContextHandler();
        mainContextHandler.setContextPath("/");
        mainContextHandler.setResourceBase("target/classes/static");


        final FreeMarkerTemplateEngine templateEngine = new FreeMarkerTemplateEngine("/product/");
        templateEngine.init();

        TemplateProcessor templateProcessor = new TemplateProcessor("product/");

        templatorEngine = new TemplatorEngine(templateProcessor);

        Properties queries = PropertyReader.readPropertyFile("query.properties");
        LOGGER.info("Query entries: {}", queries.size());
        Properties dataSourceProperties = JdbcPropertyResolver.resolve();
        LOGGER.info("dsProperties entries: {}", dataSourceProperties.size());
        JdbcProductDao productDao = new JdbcProductDao(new JdbcDataSourceFactory(dataSourceProperties).getDataSource());
        //productDao.setQueries(queries);
        ProductService productService = new ProductService(productDao);

        initServlets(mainContextHandler, productService, templateEngine);
        initFilters(mainContextHandler);

        initErrorHandler(mainContextHandler);

        server.setHandler(mainContextHandler);
    }

    private static void initFilters(ServletContextHandler mainContextHandler) {
        mainContextHandler.addFilter(LoggingFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
    }

    private static void initServlets(ServletContextHandler mainContextHandler, ProductService productService, TemplateEngine templateEngine) {
        AllProductsServlet allProductsServlet = new AllProductsServlet(productService, templatorEngine);
        GetProductServlet getProductServlet = new GetProductServlet(productService, templatorEngine);
        ProductFormHandlerServlet productFormHandlerServlet = new ProductFormHandlerServlet(productService, templateEngine);
        ErrorHandlerServlet errorHandlerServlet = new ErrorHandlerServlet(templateEngine);
        SearchServlet searchProductServlet = new SearchServlet(productService, templatorEngine);
        DefaultServlet defaultServlet = new DefaultServlet();

        mainContextHandler.addServlet(new ServletHolder(allProductsServlet), "");
        mainContextHandler.addServlet(new ServletHolder(getProductServlet), "/product/*");
        mainContextHandler.addServlet(new ServletHolder(productFormHandlerServlet), "/product/edit/*");
        mainContextHandler.addServlet(new ServletHolder(productFormHandlerServlet), "/product/add");
        mainContextHandler.addServlet(new ServletHolder(errorHandlerServlet), "/errorHandler");
        mainContextHandler.addServlet(new ServletHolder(searchProductServlet), "/search");
        mainContextHandler.addServlet(new ServletHolder(defaultServlet), "/");
    }

    private static void initErrorHandler(ServletContextHandler mainContextHandler) {
        ErrorPageErrorHandler handler404error = new ErrorPageErrorHandler();
        mainContextHandler.setErrorHandler(handler404error);
        handler404error.addErrorPage(404, "/notFound.html");
        handler404error.addErrorPage(Exception.class, "/errorHandler");
    }

}
