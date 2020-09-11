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

    public boolean validateProduct(ProductDTO productToValidate, Map<String, Object> responseMap) {
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
                responseMap.put("validationWarning", validationWarningList);
                return false;
            }
            if (new BigDecimal(productToValidate.getPrice()).compareTo(new BigDecimal(0)) <= 0) {
                validationWarningList.add("Product Price should be set to the value greater than 0;");
            }
        }
        if (!validationWarningList.isEmpty()) {
            responseMap.put("validationWarning", validationWarningList);
            prepareResponseMap(responseMap, productToValidate);
            return false;
        }
        return true;
    }

    public Product getNewProduct(Map<String, Object> responseMap) {
        Product newProduct = new Product();

        ProductDTO productDTO = ProductConverter.fromProduct(newProduct);
        prepareResponseMap(responseMap, productDTO);
        return newProduct;
    }

    public Product getProductById(HttpServletRequest req, Map<String, Object> responseMap) {
        int idParameterFromUrl = getParameterFromUrl(req);
        Product newProduct = Optional.ofNullable(productDao.getOneById(idParameterFromUrl)).orElse(null);

        ProductDTO productDTO = ProductConverter.fromProduct(newProduct);
        prepareResponseMap(responseMap, productDTO);
        return newProduct;
    }

    public void searchProductByKeyWord(HttpServletRequest req, Map<String, Object> responseMap) {
        String keyWord = req.getParameter("keyWord");
        if (keyWord == null) {
            return;
        }
        List<ProductDTO> productsList;
        productsList = convertToListOfDTO(productDao.searchProducts(keyWord));
        responseMap.put("keyWord", keyWord);
        responseMap.put("productCount", productsList.size());
        responseMap.put("productList", productsList);
        responseMap.put("includeSearchForm", true);
    }

    private List<ProductDTO> convertToListOfDTO(List<Product> listOfProducts) {
        return listOfProducts
                .stream()
                .map(ProductConverter::fromProduct)
                .collect(toList());
    }

    public void getAll(Map<String, Object> responseMap) {
        List<ProductDTO> productsList = convertToListOfDTO(productDao.getAll());
        responseMap.put("productCount", productsList.size());
        responseMap.put("productList", productsList);
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

        if (!req.getRequestURI().equals("/product/add")) {
            productDTO.setVersionId(productVersion);
        }
        return productDTO;
    }

    String getFormActionByProduct(ProductDTO product) {
        if (product.getVersionId() == null) {
            return "/product/add";
        } else {
            return "/product/edit/" + product.getId();
        }
    }

    public void processProductFormSubmission(HttpServletRequest req, Map<String, Object> responseMap) {
        ProductDTO newProduct = getProductFromRequest(req);
        if (!validateProduct(newProduct, responseMap)) {
            return;
        }
        if (newProduct.getVersionId() != null) {
            processExistingProduct(req.getRequestURI(), responseMap, newProduct);
        } else {
            processNewProduct(responseMap, newProduct);
        }
    }

    void processExistingProduct(String requestURI, Map<String, Object> responseMap, ProductDTO newProduct) {
        List<String> validationErrorList = new ArrayList<>();
        Product product = ProductConverter.toProduct(newProduct);
        ProductDTO updatedProduct = ProductConverter.fromProduct(productDao.updateOne(product));
        prepareResponseMap(responseMap, updatedProduct, getFormActionByProduct(newProduct));
        if (!validateVersion(product.getVersionId(), updatedProduct.getVersionId())) {
            validationErrorList.add("The product you are trying to save was updated by someone else. Please <a href='" + requestURI + "'>refresh</a> and try again");
            responseMap.put("validationWarning", validationErrorList);
        }
    }

    static boolean validateVersion(String oldProductVersion, String updatedProductVersion) {
        return !Objects.equals(updatedProductVersion, oldProductVersion);
    }

    void processNewProduct(Map<String, Object> responseMap, ProductDTO newProduct) {
        Product product = ProductConverter.toProduct(newProduct);
        ProductDTO insertedProductDTO = ProductConverter.fromProduct(productDao.insertOne(product));
        prepareResponseMap(responseMap, insertedProductDTO);
    }

    void prepareResponseMap(Map<String, Object> responseMap, ProductDTO product) {
        prepareResponseMap(responseMap, product, getFormActionByProduct(product));
    }

    void prepareResponseMap(Map<String, Object> responseMap, ProductDTO product, String formAction) {
        responseMap.put("product", product);
        responseMap.put("formAction", formAction);
    }
}
