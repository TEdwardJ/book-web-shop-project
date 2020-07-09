package edu.ted.webshop.controller;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.ProductDTO;
import edu.ted.webshop.utils.TemplateEngine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductController {

    private final JdbcProductDao productDao = JdbcProductDao.getInstance();
    private final TemplateEngine templateEngine = TemplateEngine.getInstance();

    private boolean isEmptyOrNull(String field) {
        return field == null || field.isEmpty();
    }

    public List<String> validateProduct(ProductDTO productToValidate) {

        List<String> validationErrorList = new ArrayList<>();

        if (isEmptyOrNull(productToValidate.getName())) {
            validationErrorList.add("Product Name cannot be empty;");
        }
        try{
            BigDecimal priceToValidate = new BigDecimal(productToValidate.getPrice());
        } catch (Exception e) {
            validationErrorList.add("Product price should not contains any characters except digits and delimiters");
        }
        if (new BigDecimal(productToValidate.getPrice()).compareTo(new BigDecimal(0))<=0) {
            validationErrorList.add("Product Price should be set to the value greater than 0;");
        }
        if (productToValidate.getPrice().isEmpty() ) {
            validationErrorList.add("Product Price should be specified;");
        }
        return validationErrorList;
    }

}
