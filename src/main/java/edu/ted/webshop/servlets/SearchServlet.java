package edu.ted.webshop.servlets;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.utils.TemplateEngine;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "SearchProductServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {


    public SearchServlet() throws IOException {
    }

    JdbcProductDao productDao = new JdbcProductDao();
    private TemplateEngine templateEngine = TemplateEngine.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        String keyWord = Optional.ofNullable(req.getParameter("keyWord")).orElse("");
        Map map = new HashMap();
        List<Product> productsList;
        if (keyWord.isEmpty()) {
            resp.sendRedirect("/");
            return;
        } else {
            productsList = productDao.searchProducts(keyWord);
        }
        map.put("keyWord", keyWord);
        map.put("productCount", productsList.size());
        map.put("productList", productsList);
        map.put("includeSearchForm", true);

        templateEngine.writePage("index.html", resp.getWriter(), map);
    }
}
