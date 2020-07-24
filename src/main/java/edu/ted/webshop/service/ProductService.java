package edu.ted.webshop.service;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.web.dto.ProductDTO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductService {

    private final JdbcProductDao productDao;

    public ProductService(JdbcProductDao productDao) {
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
                new BigDecimal(productToValidate.getPrice());
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
        map.put("formAction", getFormAction(newProduct));
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

    public void getAll(Map<String, Object> map) {
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
        String productVersion = req.getParameter("versionId");
        final ProductDTO productDTO = new ProductDTO(productId, productName, productDescription, pictureUrl, price);
        productDTO.setVersionId(productVersion);
        return productDTO;
    }

    String getFormAction(ProductDTO product) {
        if (product.getId().equals("0")) {
            return "/product/add";
        } else {
            return "/product/edit/" + product.getId();
        }
    }

    String getFormAction(Product product) {
        if (product == null || product.getId() == 0) {
            return "/product/add";
        } else {
            return "/product/edit/" + product.getId();
        }
    }

    public void processProductFormSubmission(HttpServletRequest req, Map<String, Object> map) {
        ProductDTO newProduct = getProductFromRequest(req);
        List<String> validationWarningList = validateProduct(newProduct);
        if (!validationWarningList.isEmpty()) {
            map.put("validationWarning", validationWarningList);
            map.put("product", newProduct);
            map.put("formAction", getFormAction(newProduct));
        } else {
            Product product = newProduct.getProduct();
            if (product.getId() != 0) {
                map.put("formAction", getFormAction(product));
                String oldProductVersion = Optional.ofNullable(product.getVersionId()).orElse("");
                final Product updatedProduct = productDao.updateOne(product);
                map.put("product", updatedProduct);
                if (updatedProduct.getVersionId().equals(oldProductVersion)) {
                    validationWarningList.add("The product you try to save was updated by someone else. Please <a href='" + req.getRequestURI() + "'>refresh</a> and try again");
                    map.put("validationWarning", validationWarningList);
                }
            } else {
                final Product insertedProduct = productDao.insertOne(product);
                map.put("product", insertedProduct);
                map.put("formAction", getFormAction(insertedProduct));
            }
        }
    }
}
