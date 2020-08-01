package edu.ted.webshop.web.servlet;

import edu.ted.webshop.service.ProductService;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.utils.FreeMarkerTemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "GetProductServlet", urlPatterns = "/product/*")
public class GetProductServlet extends HttpServlet {

    private ProductService productService;
    private FreeMarkerTemplateEngine templateEngine;

    public GetProductServlet(ProductService productService, FreeMarkerTemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> map = new HashMap<>();
        final Product productById = productService.getProductById(req, map);

        if (productById == null) {
            resp.setContentLength(0);
            resp.sendRedirect("/notFound.html");
            return;
        }

        templateEngine.writePage("product.html", resp.getWriter(), map);
    }
}
