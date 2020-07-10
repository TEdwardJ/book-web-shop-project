package edu.ted.webshop.server;

import edu.ted.webshop.servlets.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;


import javax.servlet.DispatcherType;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;
import java.util.Optional;

public class WebShopServer {

    private Server server;

    public void start() throws Exception {
        System.out.println(System.getenv("PORT") + " is the port");

        Integer port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8081"));

        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});

        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.setContextPath("/");

        ServletContextHandler staticResourcesContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        staticResourcesContext.setContextPath("/stat");
        //staticResourcesContext.setResourceBase("target/classes/static");
        //URL res = Server.class.getClassLoader().getResource("static/contacts.html");
        URL res = Server.class.getClassLoader().getResource("static/contacts.html");
        URI webRootUri = res.toURI();
        System.out.println("!????!:="+res);
        System.out.println("!????!webRootUri:= "+webRootUri);
        final String canonicalPath = new File(".").getCanonicalPath();
        webRootUri = URI.create(webRootUri.toASCIIString().replaceFirst("/contacts.html$","/"));
        System.out.println("webRootUri normalized: "+ webRootUri);
        System.out.println("Canonical: "+ canonicalPath);
        final Resource resourceStat = Resource.newResource(webRootUri);
        staticResourcesContext.setBaseResource(resourceStat);
        System.out.println(staticResourcesContext.getBaseResource());
        staticResourcesContext.setHandler(new ResourceHandler());

        servletContext.addServlet(new ServletHolder(new AllProductsServlet()), "/product/all");
        servletContext.addServlet(new ServletHolder(new GetProductServlet()), "/product/*");
        servletContext.addServlet(new ServletHolder(new ProductFormHandlerServlet()), "/product/edit/*");
        servletContext.addServlet(new ServletHolder(new ProductFormHandlerServlet()), "/product/add");
        servletContext.addServlet(new ServletHolder(new ErrorHandlerServlet()), "/errorHandler");
        servletContext.addServlet(new ServletHolder(new SearchServlet()), "/search");

        servletContext.addFilter(new FilterHolder(new GetProductRedirectFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));
        ContextHandlerCollection contexts = new ContextHandlerCollection(
                servletContext, staticResourcesContext
        );
        server.setHandler(contexts);
        server.start();

    }
}
