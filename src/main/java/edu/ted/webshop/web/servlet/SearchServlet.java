package edu.ted.webshop.web.servlet;

import edu.ted.webshop.service.ProductService;
import edu.ted.webshop.utils.FreeMarkerTemplateEngine;
import edu.ted.webshop.utils.interfaces.TemplateEngine;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "SearchProductServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {

    private ProductService productService;
    private TemplateEngine templateEngine;

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
            resp.sendRedirect("/");
        }
        templateEngine.writePage("index.html", resp.getWriter(), map);
    }

}
