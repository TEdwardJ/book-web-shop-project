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
import java.util.*;

@WebServlet(name = "editProductServlet", urlPatterns = {"/product/edit/*", "/product/add"})
public class ProductFormHandlerServlet extends HttpServlet {

    private ProductService productService;
    private FreeMarkerTemplateEngine templateEngine;

    public ProductFormHandlerServlet(ProductService productService, FreeMarkerTemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<>();
        productService.processProductFormSubmission(req, map);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        templateEngine.writePage("productForm.html", resp.getWriter(), map);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> map = new HashMap<>();
        Product productById = productService.getProductById(req, map);

        if (productById == null) {
            resp.setContentLength(0);
            resp.sendRedirect("/notFound.html");
            return;
        }

        templateEngine.writePage("productForm.html", resp.getWriter(), map);
    }

}
