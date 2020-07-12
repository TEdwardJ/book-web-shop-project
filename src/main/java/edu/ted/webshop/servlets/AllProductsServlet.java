package edu.ted.webshop.servlets;

import edu.ted.webshop.controller.ProductController;
import edu.ted.webshop.dao.JdbcProductDao;
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

@WebServlet(name = "AllProductsServlet", urlPatterns = {"/product/all"})
public class AllProductsServlet extends HttpServlet {

    private  ProductController productController;

    private TemplateEngine templateEngine = TemplateEngine.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map map = new HashMap();
        productController.getAll(map);

        templateEngine.writePage("index.html", resp.getWriter(), map);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productController = (ProductController) this.getServletContext().getAttribute("productController");

    }
}
