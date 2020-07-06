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
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "editProductServlet", urlPatterns = {"/product/edit/*", "/product/add"})
public class ProductFormHandlerServlet extends HttpServlet {

    private final JdbcProductDao productDao = new JdbcProductDao();
    private final TemplateEngine templateEngine = TemplateEngine.getInstance();

    public ProductFormHandlerServlet() throws IOException {
    }

    private int getParameterFromUrl(HttpServletRequest req) {
        String servletPath = req.getServletPath();
        String requestURI = req.getRequestURI();
        Pattern pattern = Pattern.compile(".*" + servletPath + "/(.*)");
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private Product getProductFromRequest(HttpServletRequest req) {
        if (req.getMethod().equals("GET")) {
            int productId = getParameterFromUrl(req);
            if (productId != 0) {
                return productDao.getOneById(getParameterFromUrl(req));
            } else {
                return new Product();
            }
        }

        String productId = req.getParameter("id");

        String productName = req.getParameter("name");
        String productDescription = req.getParameter("description");
        String pictureUrl = req.getParameter("pictureUrl");
        String price = Optional.ofNullable(req.getParameter("price")).orElse("0");
        BigDecimal productPrice = new BigDecimal(price.replace(",", "."));
        return new Product(Integer.valueOf(productId), productName, productDescription, pictureUrl, productPrice);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product newProduct = getProductFromRequest(req);

        Map map = new HashMap();
        map.put("product", newProduct);
        List<String> validationWarning = new ArrayList<>();
        if (req.getAttribute("validationWarning") != null) {
            validationWarning.addAll((List<String>) req.getAttribute("validationWarning"));
        }
        if (validationWarning.size() == 0) {
            if (newProduct.getId() != 0) {
                map.put("product", productDao.updateOne(newProduct));
            } else {
                map.put("product", productDao.insertOne(newProduct));
            }
        } else {
            map.put("validationWarning", validationWarning);
        }
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        templateEngine.writePage("productForm.html", resp.getWriter(), map);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        Product newProduct = getProductFromRequest(req);

        Map map = new HashMap();
        map.put("product", newProduct);
        templateEngine.writePage("productForm.html", resp.getWriter(), map);
    }
}