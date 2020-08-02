package edu.ted.webshop.web.servlet;

import edu.ted.webshop.service.ProductService;
import edu.ted.webshop.utils.FreeMarkerTemplateEngine;
import edu.ted.webshop.utils.interfaces.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AllProductsServlet", urlPatterns = {"/product/all"})
public class AllProductsServlet extends HttpServlet {

    private ProductService productService;
    private TemplateEngine templateEngine;

    public AllProductsServlet(ProductService productService,TemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> map = new HashMap<>();
        productService.getAll(map);
        map.put("keyWord", "");
        templateEngine.writePage("index.html", resp.getWriter(), map);
    }

}
