package edu.ted.webshop.server;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.*;


public class WebShopServer {

    private Server server;

    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8081);
        server.setConnectors(new Connector[]{connector});
        WebAppContext context = new WebAppContext();
        context.setConfigurations(new Configuration[] {
                new AnnotationConfiguration(), new WebXmlConfiguration(),
                new WebInfConfiguration(),
                new PlusConfiguration(), new MetaInfConfiguration(),
                new FragmentConfiguration(), new EnvConfiguration() });
        context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        context.setContextPath("/");
        context.setWar("target/web-shop-project-1.0-SNAPSHOT.war");
        ///context.getMetaData().addContainerResource(new PathResource(new File("./target/classes").toURI()));
        //context.getMetaData().setWebInfClassesDirs();
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setAttribute(AnnotationConfiguration.MULTI_THREADED, Boolean.FALSE);
        context.setAttribute(AnnotationConfiguration.MAX_SCAN_WAIT, 20);

        ContextHandlerCollection contexts = new ContextHandlerCollection(
                context
        );
        server.setHandler(contexts);
        server.start();
    }

}
