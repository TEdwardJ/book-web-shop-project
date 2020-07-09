package edu.ted.webshop.server;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.*;

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
        //WebAppContext webContext = new WebAppContext("target/web-shop-project-1.0-SNAPSHOT.war", "/");
        WebAppContext webContext = new WebAppContext("src/main/webapp", "/");
        webContext.setConfigurations(new Configuration[] {
                new AnnotationConfiguration(), new WebXmlConfiguration(),
                new WebInfConfiguration(),
                new PlusConfiguration(), new MetaInfConfiguration(),
                new FragmentConfiguration(), new EnvConfiguration() });
        webContext.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        //context.setContextPath("/");
        //context.setWar("target/web-shop-project-1.0-SNAPSHOT.war");
        //context.setWar("target/web-shop-project-1.0-SNAPSHOT.one-jar.jar");
        ///context.getMetaData().addContainerResource(new PathResource(new File("./target/classes").toURI()));
        //context.getMetaData().setWebInfClassesDirs();
        webContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        webContext.setAttribute(AnnotationConfiguration.MULTI_THREADED, Boolean.FALSE);
        webContext.setAttribute(AnnotationConfiguration.MAX_SCAN_WAIT, 20);
        //ServletContextHandler servletHandler = new ServletContextHandler();
        //webContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/classes/.*");
        ContextHandlerCollection contexts = new ContextHandlerCollection(
                webContext
        );
        server.setHandler(contexts);
        server.start();
    }

}
