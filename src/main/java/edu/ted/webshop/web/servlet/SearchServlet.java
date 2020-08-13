package edu.ted.webshop.web.servlet;

import edu.ted.webshop.service.ProductService;
import edu.ted.webshop.utils.interfaces.TemplateEngine;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "SearchProductServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    private final ProductService productService;
    private final TemplateEngine templateEngine;

    public SearchServlet(ProductService productService, TemplateEngine templateEngine) {
        this.productService = productService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> map = new HashMap<>();

        productService.searchProductByKeyWord(req, map);
        if (!map.containsKey("keyWord")) {
            resp.setContentLength(0);
            resp.sendRedirect("/");
            return;
        }
        templateEngine.writePage("index.html", resp.getWriter(), map);
    }

}
