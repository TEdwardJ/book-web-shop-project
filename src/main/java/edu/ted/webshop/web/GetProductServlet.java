package edu.ted.webshop.web;

import edu.ted.webshop.controller.ProductController;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.utils.TemplateEngine;

import javax.servlet.ServletConfig;
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

    private ProductController productController;

    private TemplateEngine templateEngine;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> map = new HashMap<>();
        final Product productById = productController.getProductById(req, map);

        if (productById == null) {
            resp.sendRedirect("/notFound.html");
            return;
        }

        templateEngine.writePage("product.html", resp.getWriter(), map);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productController = (ProductController) this.getServletContext().getAttribute("productController");
        templateEngine = (TemplateEngine) this.getServletContext().getAttribute("templateEngine");
    }
}
