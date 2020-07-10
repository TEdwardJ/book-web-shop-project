package edu.ted.webshop.server;

import edu.ted.webshop.servlets.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.PathResource;


import javax.servlet.DispatcherType;
import java.io.File;
import java.nio.file.Path;
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

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        ContextHandler ctxHandler = new ContextHandler();
        ctxHandler.setContextPath("/");
        ResourceHandler resource = new ResourceHandler();
        resource.setBaseResource(new PathResource(new File("webapp")));
        ctxHandler.setHandler(resource);
        context.addServlet(new ServletHolder(new AllProductsServlet()), "/product/all");
        context.addServlet(new ServletHolder(new GetProductServlet()), "/product/*");
        context.addServlet(new ServletHolder(new ProductFormHandlerServlet()), "/product/edit/*");
        context.addServlet(new ServletHolder(new ProductFormHandlerServlet()), "/product/add");
        context.addServlet(new ServletHolder(new ErrorHandlerServlet()), "/errorHandler");
        context.addServlet(new ServletHolder(new SearchServlet()), "/search");

        context.addFilter(new FilterHolder(new GetProductRedirectFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));
        ContextHandlerCollection contexts = new ContextHandlerCollection(
                context,ctxHandler
        );
        server.setHandler(contexts);
        server.start();

    }
}
