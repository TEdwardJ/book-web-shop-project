package edu.ted.webshop;

import edu.ted.webshop.server.WebShopServer;
import edu.ted.webshop.server.WebShopServerClassic;
import edu.ted.webshop.server.WebShopServerNew;

public class Starter {
    public static void main(String[] args) throws Exception {
        WebShopServerClassic server = new WebShopServerClassic();
        server.start();
    }
}
