package edu.ted.webshop.server;

import edu.ted.webshop.web.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.*;


import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.Optional;

public class WebShopServer extends Server {

    public void init() {
        System.out.println(System.getenv("PORT")+" is the port");

        Integer port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8081"));

        ServerConnector connector = new ServerConnector(this);
        connector.setPort(port);
        this.setConnectors(new Connector[]{connector});

        ServletContextHandler mainContextHandler = new ServletContextHandler();
        mainContextHandler.addEventListener(new ConfigContextListener());
        mainContextHandler.setContextPath("/");
        mainContextHandler.setResourceBase("target/classes/static");

        initServlets(mainContextHandler);
        initFilters(mainContextHandler);

        initErrorHandler(mainContextHandler);

        //mainContextHandler.setErrorHandler(handler404error);

        this.setHandler(mainContextHandler);

    }

    private void initFilters(ServletContextHandler mainContextHandler) {
        mainContextHandler.addFilter(LoggingFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
    }

    private void initServlets(ServletContextHandler mainContextHandler) {
        mainContextHandler.addServlet(new ServletHolder(new AllProductsServlet()), "");
        //mainContextHandler.addServlet(AllProductsServlet.class, "/product/all");
        mainContextHandler.addServlet(GetProductServlet.class, "/product/*");
        mainContextHandler.addServlet(ProductFormHandlerServlet.class, "/product/edit/*");
        mainContextHandler.addServlet(ProductFormHandlerServlet.class, "/product/add");
        mainContextHandler.addServlet(ErrorHandlerServlet.class, "/errorHandler");
        mainContextHandler.addServlet(SearchServlet.class, "/search");
        mainContextHandler.addServlet(DefaultServlet.class, "/");
    }

    private void initErrorHandler(ServletContextHandler mainContextHandler) {
        ErrorPageErrorHandler handler404error = new ErrorPageErrorHandler();
        mainContextHandler.setErrorHandler(handler404error);
        handler404error.addErrorPage(404,"/notFound.html");
        handler404error.addErrorPage(Exception.class,"/errorHandler");
    }
}
