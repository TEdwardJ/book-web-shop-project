package edu.ted.webshop.web;

import edu.ted.webshop.Starter;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class ServletsITest {

    private static Server server;

    public static HttpClient client;

    @BeforeAll
    public static void startServer() throws Exception
    {
        /*server = new WebShopServer();
        server.init();
        server.start();*/
        Starter.main(new String[]{});
        server = Starter.getServer();

        client = new HttpClient();
        client.start();
    }

    @AfterAll
    public static void stopServer() throws Exception {
        server.stop();
        client.stop();
    }

    @Test
    public void allProductsDoGet() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {

        URI uri = new URI("http://127.0.0.1:8081/").resolve("/");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.GET)
                .send();
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));

        // test response content
        String responseBody = response.getContentAsString();
        assertThat("Response Content", responseBody, containsString("General Books List"));
    }

    @Test
    public void searchProductDoGet() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {

        URI uri = new URI("http://127.0.0.1:8081/search?keyWord=King");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.GET)
                .send();
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));

        // test response content
        assertThat("Content Type", response.getMediaType(), is("text/html"));
        String responseBody = response.getContentAsString();
        assertThat("Response Content", responseBody, containsString("General Books List"));
    }

    @Test
    public void searchProductWithNoKeyWordDoGet() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        URI uri = new URI("http://127.0.0.1:8081/search?");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.GET)
                .send();
        // test response content
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));
        assertThat("HTTP Redirect to home page", response.getRequest().getPath(), is("/"));
    }

    @Test
    public void getProductDoGet() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        URI uri = new URI("http://127.0.0.1:8081/product/28");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.GET)
                .send();
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));

        // test response content
        assertThat("Content Type", response.getMediaType(), is("text/html"));
        String responseBody = response.getContentAsString();
        assertThat("Response Content", responseBody, containsString("<title>Product Info</title>"));
    }

    @Test
    public void editProductDoGet() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        URI uri = new URI("http://127.0.0.1:8081/product/edit/28");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.GET)
                .send();
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));

        // test response content
        assertThat("Content Type", response.getMediaType(), is("text/html"));
        String responseBody = response.getContentAsString();
        assertThat("Response Content: Form", responseBody, containsString("<form method=\"post\""));
        assertThat("Response Content: productId", responseBody, containsString("<INPUT type=\"hidden\" name=\"id\" value=\"28\"/>"));
    }

    @Test
    public void addProductDoGet() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        URI uri = new URI("http://127.0.0.1:8081/product/add");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.GET)
                .send();
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));

        // test response content
        assertThat("Content Type", response.getMediaType(), is("text/html"));
        String responseBody = response.getContentAsString();
        assertThat("Response Content: Form", responseBody, containsString("<form method=\"post\""));
        assertThat("Response Content: Form", responseBody, containsString("action=\"/product/add\""));
        assertThat("Response Content: productId", responseBody, containsString("<INPUT type=\"hidden\" name=\"id\" value=\"0\"/>"));
    }

    @Test
    public void editProductDoPost() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        URI uri = new URI("http://127.0.0.1:8081/product/edit/28");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.POST)
                .param("id", "28")
                .param("name", "The Bazaar of Bad Dreams Export")
                .param("description", "Stephen King. The Bazaar of Bad Dreams Export")
                .param("pictureUrl", "https://book-ye.com.ua/upload/resize_cache/iblock/1f5/230_355_1/91c36244_917f_11e7_80cf_000c29ae1566_469831ce_a092_11e7_80d1_000c29ae1566.jpg")
                .param("price", "222")
                .send();
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));

        // test response content
        assertThat("Content Type", response.getMediaType(), is("text/html"));
        String responseBody = response.getContentAsString();
        assertThat("Response Content: Form", responseBody, containsString("<form method=\"post\""));
        assertThat("Response Content: Updated Product Price (222 instead of 262)", responseBody, containsString("<INPUT type=\"text\" id=\"productPriceField\" name=\"price\" value=\"222\"/>"));
    }

    @Test
    public void editNonExistingProductDoGet() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        URI uri = new URI("http://127.0.0.1:8081/product/edit/288");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.GET)
                .send();
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));

        // test response content
        assertThat("Content Type", response.getMediaType(), is("text/html"));
        assertThat("Request Path", response.getRequest().getPath(), is("/notFound.html"));
        String responseBody = response.getContentAsString();
        assertThat("Response Content", responseBody, containsString("Not Found"));
    }

    @Test
    public void getNonExistingProductDoGet() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        URI uri = new URI("http://127.0.0.1:8081/product/288");
        ContentResponse response = client.newRequest(uri)
                .method(HttpMethod.GET)
                .send();
        assertThat("HTTP Response Status", response.getStatus(), is(HttpStatus.OK_200));

        // test response content
        assertThat("Content Type", response.getMediaType(), is("text/html"));
        assertThat("Request Path", response.getRequest().getPath(), is("/notFound.html"));
        String responseBody = response.getContentAsString();
        assertThat("Response Content", responseBody, containsString("Not Found"));
    }
}