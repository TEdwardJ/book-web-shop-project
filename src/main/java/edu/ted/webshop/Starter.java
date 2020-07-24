package edu.ted.webshop;

import edu.ted.webshop.utils.ProductRowMapper;
import edu.ted.webshop.web.*;
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

public class Starter {

    private static Logger LOGGER = LoggerFactory.getLogger(ProductRowMapper.class);

    private static Server server;

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
        System.out.println(System.getenv("PORT")+" is the port");

        Integer port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8081"));

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});

        ServletContextHandler mainContextHandler = new ServletContextHandler();
        mainContextHandler.addEventListener(new ConfigContextListener());
        mainContextHandler.setContextPath("/");
        mainContextHandler.setResourceBase("target/classes/static");

        initServlets(mainContextHandler);
        initFilters(mainContextHandler);

        initErrorHandler(mainContextHandler);

        server.setHandler(mainContextHandler);
    }


    private static void initFilters(ServletContextHandler mainContextHandler) {
        mainContextHandler.addFilter(LoggingFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
    }

    private static void initServlets(ServletContextHandler mainContextHandler) {
        mainContextHandler.addServlet(new ServletHolder(new AllProductsServlet()), "");
        mainContextHandler.addServlet(GetProductServlet.class, "/product/*");
        mainContextHandler.addServlet(ProductFormHandlerServlet.class, "/product/edit/*");
        mainContextHandler.addServlet(ProductFormHandlerServlet.class, "/product/add");
        mainContextHandler.addServlet(ErrorHandlerServlet.class, "/errorHandler");
        mainContextHandler.addServlet(SearchServlet.class, "/search");
        mainContextHandler.addServlet(DefaultServlet.class, "/");
    }

    private static void initErrorHandler(ServletContextHandler mainContextHandler) {
        ErrorPageErrorHandler handler404error = new ErrorPageErrorHandler();
        mainContextHandler.setErrorHandler(handler404error);
        handler404error.addErrorPage(404,"/notFound.html");
        handler404error.addErrorPage(Exception.class,"/errorHandler");
    }

}
