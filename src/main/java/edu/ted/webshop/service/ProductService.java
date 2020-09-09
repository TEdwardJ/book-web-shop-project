package edu.ted.webshop.service;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.web.dto.ProductConverter;
import edu.ted.webshop.web.dto.ProductDTO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class ProductService {

    private final JdbcProductDao productDao;

    public ProductService(JdbcProductDao productDao) {
        this.productDao = productDao;
    }

    private boolean isEmptyOrNull(String field) {
        return field == null || field.isEmpty();
    }

    public boolean validateProduct(ProductDTO productToValidate, Map<String, Object> parametersMap) {
        List<String> validationWarningList = new ArrayList<>();

        if (isEmptyOrNull(productToValidate.getName())) {
            validationWarningList.add("Product Name cannot be empty;");
        }
        if (productToValidate.getPrice().isEmpty()) {
            validationWarningList.add("Product Price should be specified;");
        } else {
            try {
                new BigDecimal(productToValidate.getPrice());
            } catch (Exception e) {
                validationWarningList.add("Product Price should not contain any characters except digits and delimiters;");
                parametersMap.put("validationWarning", validationWarningList);
                return false;
            }
            if (new BigDecimal(productToValidate.getPrice()).compareTo(new BigDecimal(0)) <= 0) {
                validationWarningList.add("Product Price should be set to the value greater than 0;");
            }
        }
        if (!validationWarningList.isEmpty()) {
            parametersMap.put("validationWarning", validationWarningList);
            parametersMap.put("product", productToValidate);
            parametersMap.put("formAction", getFormActionByProduct(productToValidate));
            return false;
        }
        return true;
    }

    public Product getProductById(HttpServletRequest req, Map<String, Object> map) {
        Product newProduct;
        if (req.getRequestURI().equals("/product/add")) {
            newProduct = new Product();
        } else {
            newProduct = Optional.ofNullable(productDao.getOneById(getParameterFromUrl(req))).orElse(null);
        }

        ProductDTO productDTO = ProductConverter.fromProduct(newProduct);
        map.put("product", productDTO);
        map.put("formAction", getFormActionByProduct(productDTO));
        return newProduct;
    }

    public void searchProductByKeyWord(HttpServletRequest req, Map<String, Object> map) {
        String keyWord = Optional.ofNullable(req.getParameter("keyWord")).orElse("");
        if (keyWord.isEmpty()) {
            return;
        }
        List<ProductDTO> productsList;
        productsList = convertToListOfDTO(productDao.searchProducts(keyWord));
        map.put("keyWord", keyWord);
        map.put("productCount", productsList.size());
        map.put("productList", productsList);
        map.put("includeSearchForm", true);
    }

    private List<ProductDTO> convertToListOfDTO(List<Product> listOfProducts) {
        return listOfProducts
                .stream()
                .map(ProductConverter::fromProduct)
                .collect(toList());
    }

    public void getAll(Map<String, Object> map) {
        List<ProductDTO> productsList = convertToListOfDTO(productDao.getAll());
        map.put("productCount", productsList.size());
        map.put("productList", productsList);
    }

    int getParameterFromUrl(HttpServletRequest req) {
        int idFromRequest = 0;
        String servletPath = req.getServletPath();
        String requestURI = req.getRequestURI();
        Matcher matcher = Pattern.compile(".*" + servletPath + "/([0-9]*)").matcher(requestURI);
        if (matcher.matches()) {
            idFromRequest = Integer.parseInt(matcher.group(1));
        }
        return idFromRequest;
    }

    ProductDTO getProductFromRequest(HttpServletRequest req) {
        String productId = req.getParameter("id");

        String productName = req.getParameter("name");
        String productDescription = req.getParameter("description");
        String pictureUrl = req.getParameter("pictureUrl");
        String price = Optional.ofNullable(req.getParameter("price")).orElse("0");
        String productVersion = req.getParameter("versionId");
        ProductDTO productDTO = new ProductDTO(productId, productName, productDescription, pictureUrl, price);
        productDTO.setVersionId(productVersion);
        return productDTO;
    }

    String getFormActionByProduct(ProductDTO product) {
        if (product.getId().equals("0")) {
            return "/product/add";
        } else {
            return "/product/edit/" + product.getId();
        }
    }

    public void processProductFormSubmission(HttpServletRequest req, Map<String, Object> parametersMap) {
        ProductDTO newProduct = getProductFromRequest(req);
        if (!validateProduct(newProduct, parametersMap)) {
            return;
        }
        if (!newProduct.getId().equals("0")) {
            processExistingProduct(req, parametersMap, newProduct);
        } else {
            processNewProduct(parametersMap, newProduct);
        }
    }

    void processExistingProduct(HttpServletRequest req, Map<String, Object> parametersMap, ProductDTO newProduct/*, List<String> validationWarningList*/) {
        List<String> validationErrorList = new ArrayList<>();
        parametersMap.put("formAction", getFormActionByProduct(newProduct));
        Product product = ProductConverter.toProduct(newProduct);
        String oldProductVersion = Optional.ofNullable(product.getVersionId()).orElse("");
        ProductDTO updatedProduct = ProductConverter.fromProduct(productDao.updateOne(product));
        parametersMap.put("product", updatedProduct);
        if (!validateVersion(oldProductVersion, updatedProduct.getVersionId())) {
            validationErrorList.add("The product you are trying to save was updated by someone else. Please <a href='" + req.getRequestURI() + "'>refresh</a> and try again");
            parametersMap.put("validationWarning", validationErrorList);
        }
    }

    private boolean validateVersion(String oldProductVersion, String updatedProductVersion) {
        boolean result = true;
        if (Objects.equals(updatedProductVersion, oldProductVersion)) {
            result = false;
        }
        return result;
    }

    void processNewProduct(Map<String, Object> parametersMap, ProductDTO newProduct) {
        Product product = ProductConverter.toProduct(newProduct);
        ProductDTO insertedProductDTO = ProductConverter.fromProduct(productDao.insertOne(product));
        parametersMap.put("product", insertedProductDTO);
        parametersMap.put("formAction", getFormActionByProduct(insertedProductDTO));
    }
}
