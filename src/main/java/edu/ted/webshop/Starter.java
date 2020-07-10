package edu.ted.webshop;

import edu.ted.webshop.server.WebShopServer;
import edu.ted.webshop.utils.PropertyReader;

public class Starter {
    public static void main(String[] args) throws Exception {
        WebShopServer server = new WebShopServer();
        server.start();
    }


}
