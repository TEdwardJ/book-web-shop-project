package edu.ted.webshop.servlets;

import edu.ted.webshop.server.WebShopServer;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class AllProductsServletTest {

    private WebShopServer server;

    public HttpClient client;

    @BeforeEach
    public void startServer() throws Exception
    {
        server = new WebShopServer();
        server.init();
        server.start();

        client = new HttpClient();
        client.start();
    }

    @AfterEach
    public void stopServer() throws Exception
    {
        server.stop();
        client.stop();
    }

    @Test
    public void doGet() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {

        URI uri = new URI("http://127.0.0.1:8081/").resolve("/");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.GET)
                .send();
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));

        // test response content
        String responseBody = response.getContentAsString();
        assertThat("Response Content", responseBody, containsString("General Books List"));
    }
}