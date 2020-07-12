package edu.ted.webshop.servlets;

import edu.ted.webshop.controller.ProductController;
import edu.ted.webshop.utils.TemplateEngine;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "SearchProductServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    private TemplateEngine templateEngine = TemplateEngine.getInstance();
    private ProductController productController;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> map = new HashMap<>();

        productController.searchProductByKeyWord(req, map);
        if (!map.containsKey("keyWord")) {
            resp.sendRedirect("/");
        }
        templateEngine.writePage("index.html", resp.getWriter(), map);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productController = (ProductController) this.getServletContext().getAttribute("productController");
    }
}
