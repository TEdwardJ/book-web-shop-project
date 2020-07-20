package edu.ted.webshop.controller;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.entity.ProductDTO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductController {

    private final JdbcProductDao productDao;

    public ProductController(JdbcProductDao productDao) {
        this.productDao = productDao;
    }

    private boolean isEmptyOrNull(String field) {
        return field == null || field.isEmpty();
    }

    public List<String> validateProduct(ProductDTO productToValidate) {

        List<String> validationErrorList = new ArrayList<>();

        if (isEmptyOrNull(productToValidate.getName())) {
            validationErrorList.add("Product Name cannot be empty;");
        }
        if (productToValidate.getPrice().isEmpty()) {
            validationErrorList.add("Product Price should be specified;");
        } else {
            try {
                BigDecimal priceToValidate = new BigDecimal(productToValidate.getPrice());
            } catch (Exception e) {
                validationErrorList.add("Product Price should not contains any characters except digits and delimiters;");
                return validationErrorList;
            }
            if (new BigDecimal(productToValidate.getPrice()).compareTo(new BigDecimal(0)) <= 0) {
                validationErrorList.add("Product Price should be set to the value greater than 0;");
            }
        }

        return validationErrorList;
    }

    public Product getProductById(HttpServletRequest req, Map<String, Object> map) {
        Product newProduct;
        int productId = getParameterFromUrl(req);

        if (productId > 0) {
            newProduct = productDao.getOneById(productId);
        } else {
            newProduct = new Product();
        }
        map.put("product", newProduct);
        return newProduct;
    }

    public void searchProductByKeyWord(HttpServletRequest req, Map<String, Object> map) {
        String keyWord = Optional.ofNullable(req.getParameter("keyWord")).orElse("");
        List<Product> productsList;
        if (keyWord.isEmpty()) {
            return;
        } else {
            productsList = productDao.searchProducts(keyWord);
        }
        map.put("keyWord", keyWord);
        map.put("productCount", productsList.size());
        map.put("productList", productsList);
        map.put("includeSearchForm", true);
    }

    public void getAll(Map<String, Object> map){
        List<Product> productsList = productDao.getAll();
        map.put("productCount", productsList.size());
        map.put("productList", productsList);
    }

    int getParameterFromUrl(HttpServletRequest req) {
        String servletPath = req.getServletPath();
        String requestURI = req.getRequestURI();
        Pattern pattern = Pattern.compile(".*" + servletPath + "/(.*)");
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    ProductDTO getProductFromRequest(HttpServletRequest req) {
        String productId = req.getParameter("id");

        String productName = req.getParameter("name");
        String productDescription = req.getParameter("description");
        String pictureUrl = req.getParameter("pictureUrl");
        String price = Optional.ofNullable(req.getParameter("price")).orElse("0");
        return new ProductDTO(productId, productName, productDescription, pictureUrl, price);
    }

    public void processProductFormSubmission(HttpServletRequest req, Map<String, Object> map) {
        ProductDTO newProduct = getProductFromRequest(req);
        List<String> validationWarningList = validateProduct(newProduct);
        if (!validationWarningList.isEmpty()) {
            map.put("validationWarning", validationWarningList);
            map.put("product", newProduct);
        } else {
            Product product = newProduct.getProduct();
            if (product.getId() != 0) {
                map.put("product", productDao.updateOne(product));
            } else {
                map.put("product", productDao.insertOne(product));
            }
        }
    }
}
