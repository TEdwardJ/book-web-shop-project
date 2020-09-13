package edu.ted.webshop.web.servlet;

import edu.ted.webshop.service.ProductService;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.utils.interfaces.TemplateEngine;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "editProductServlet", urlPatterns = {"/product/edit/*", "/product/add"})
public class ProductFormHandlerServlet extends HttpServlet {

    private final ProductService productService;
    private final TemplateEngine templateEngine;

    public ProductFormHandlerServlet(ProductService productService, TemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Product productById;
        if (!req.getRequestURI().equals("/product/add")) {
            productById = productService.getProductById(req, map);
            if (productById == null) {
                resp.setContentLength(0);
                resp.sendRedirect("/notFound.html");
                return;
            }
        }
        productService.processProductFormSubmission(req, map);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        templateEngine.writePage("productForm.html", resp.getWriter(), map);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> map = new HashMap<>();
        Product productById;
        if (req.getRequestURI().equals("/product/add")) {
            productService.getNewProduct(map);
        } else {
            productById = productService.getProductById(req, map);
            if (productById == null) {
                resp.setContentLength(0);
                resp.sendRedirect("/notFound.html");
                return;
            }
        }

        templateEngine.writePage("productForm.html", resp.getWriter(), map);
    }

}
