package edu.ted.webshop.servlets;

import edu.ted.webshop.controller.ProductController;
import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.entity.ProductDTO;
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

    private final JdbcProductDao productDao = JdbcProductDao.getInstance();
    private final TemplateEngine templateEngine = TemplateEngine.getInstance();

    private final ProductController productController = new ProductController();


    private boolean isEmptyOrNull(String field) {
        return field == null || field.isEmpty();
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

    private ProductDTO getProductFromRequest(HttpServletRequest req) {

        String productId = req.getParameter("id");

        String productName = req.getParameter("name");
        String productDescription = req.getParameter("description");
        String pictureUrl = req.getParameter("pictureUrl");
        String price = Optional.ofNullable(req.getParameter("price")).orElse("0");
        return new ProductDTO(productId, productName, productDescription, pictureUrl, price);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDTO newProduct = getProductFromRequest(req);
        List<String> validationWarningList = productController.validateProduct(newProduct);
        if (validationWarningList.isEmpty()) {
        } else {
            req.setAttribute("validationWarning", validationWarningList);
        }

        Map map = new HashMap();
        map.put("product", newProduct);
        List<String> validationWarning = new ArrayList<>();

        Product product = newProduct.getProduct();
        if (validationWarningList.size() == 0) {
            if (product.getId() != 0) {
                map.put("product", productDao.updateOne(product));
            } else {
                map.put("product", productDao.insertOne(product));
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
        int productId = getParameterFromUrl(req);
        Product newProduct;
        if (productId > 0) {
            newProduct = productDao.getOneById(productId);
        } else {
            newProduct = new Product();
        }
        if (newProduct == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            Map map = new HashMap();
            map.put("product", newProduct);
            templateEngine.writePage("productForm.html", resp.getWriter(), map);
        }
    }
}
