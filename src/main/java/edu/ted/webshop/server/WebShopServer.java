package edu.ted.webshop.server;

import edu.ted.webshop.servlets.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.*;


import javax.servlet.DispatcherType;
import javax.servlet.ServletContextListener;
import java.util.EnumSet;
import java.util.Optional;

public class WebShopServer {

    private Server server;

    public void start() throws Exception {
        System.out.println(System.getenv("PORT")+" is the port");

        Integer port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8081"));

        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});

        ServletContextHandler mainContextHandler = new ServletContextHandler();
        mainContextHandler.addEventListener(new ConfigContextListener());
        mainContextHandler.setContextPath("/");
        mainContextHandler.setResourceBase("target/classes/static");

        initServlets(mainContextHandler);
        initFilters(mainContextHandler);

        ErrorPageErrorHandler handler404error = initErrorHandler();

        mainContextHandler.setErrorHandler(handler404error);

        server.setHandler(mainContextHandler);
        server.start();

    }

    private void initFilters(ServletContextHandler mainContextHandler) {
        mainContextHandler.addFilter(GetProductRedirectFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
    }

    private void initServlets(ServletContextHandler mainContextHandler) {
        mainContextHandler.addServlet(new ServletHolder(new AllProductsServlet()), "");
        mainContextHandler.addServlet(AllProductsServlet.class, "/product/all");
        mainContextHandler.addServlet(GetProductServlet.class, "/product/*");
        mainContextHandler.addServlet(ProductFormHandlerServlet.class, "/product/edit/*");
        mainContextHandler.addServlet(ProductFormHandlerServlet.class, "/product/add");
        mainContextHandler.addServlet(ErrorHandlerServlet.class, "/errorHandler");
        mainContextHandler.addServlet(SearchServlet.class, "/search");
        mainContextHandler.addServlet(DefaultServlet.class, "/");
    }

    private ErrorPageErrorHandler initErrorHandler() {
        ErrorPageErrorHandler handler404error = new ErrorPageErrorHandler();
        handler404error.addErrorPage(404,"/notFound.html");
        handler404error.addErrorPage(Exception.class,"/errorHandler");
        return handler404error;
    }
}
