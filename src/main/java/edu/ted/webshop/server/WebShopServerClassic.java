package edu.ted.webshop.server;

import edu.ted.webshop.servlets.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;


import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.Optional;

public class WebShopServerClassic {


    private Server server;

    public void start() throws Exception {
        System.out.println(System.getenv("PORT")+" is the port");

        Integer port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8081"));

        server = new Server(port);
/*        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8081);
        server.setConnectors(new Connector[]{connector});*/

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //handler.setContextPath("/");
        context.addServlet(new ServletHolder(new AllProductsServlet()), "/product/all");
        context.addServlet(new ServletHolder(new GetProductServlet()), "/product/*");
        context.addServlet(new ServletHolder(new ProductFormHandlerServlet()), "/product/edit/*");
        context.addServlet(new ServletHolder(new ProductFormHandlerServlet()), "/product/add");
        context.addServlet(new ServletHolder(new ErrorHandlerServlet()), "/errorHandler");
        context.addServlet(new ServletHolder(new SearchServlet()), "/search");
        //FilterHolder allProductsFilter = new FilterHolder(new GetProductRedirectFilter());
        //context.addFilter(allProductsFilter,"/product/*", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(new FilterHolder(new GetProductRedirectFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));

        server.setHandler(context);
        server.start();
        //server.dump(System.err);
        //server.join();
    }
}
