package edu.ted.webshop.servlets;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.utils.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AllProductsServlet", urlPatterns = {"/product/all"})
public class AllProductsServlet extends HttpServlet {

    JdbcProductDao productDao = new JdbcProductDao();
    private TemplateEngine templateEngine = TemplateEngine.getInstance();

    public AllProductsServlet() throws IOException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        //String keyWord = Optional.ofNullable(req.getParameter("keyWord")).orElse("");
        Map map = new HashMap();

        List<Product> productsList = productDao.getAll();
        map.put("productCount", productsList.size());
        map.put("productList", productsList);
        templateEngine.writePage("index.html", resp.getWriter(), map);
    }
}
