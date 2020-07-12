package edu.ted.webshop.controller;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.entity.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    private JdbcProductDao productDao;
    private ProductController controller;

    @BeforeEach
    public void init() {
        productDao = mock(JdbcProductDao.class);
        controller = new ProductController(productDao);
    }

    @Test
    void givenProductWithAllFieldsFilled_whenValidate_thenCorrect() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "4.5");
        List<String> warningsList = controller.validateProduct(testedProduct);
        assertEquals(0, warningsList.size());
    }

    @Test
    void givenProductWithEmptyName_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "", "description", "", "4.5");
        List<String> warningsList = controller.validateProduct(testedProduct);
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Name cannot be empty;"));
    }

    @Test
    void givenProductWithEmptyPrice_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "");
        List<String> warningsList = controller.validateProduct(testedProduct);
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Price should be specified;"));
    }

    @Test
    void givenProductWithPriceContainingLetters_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "fdf");
        List<String> warningsList = controller.validateProduct(testedProduct);
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Price should not contains any characters except digits and delimiters;"));
    }

    @Test
    void givenProductWithZeroPrice_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "0");
        List<String> warningsList = controller.validateProduct(testedProduct);
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Price should be set to the value greater than 0;"));
    }

    @Test
    void givenProductId_whenReturnsProduct_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/product/12");
        when(req.getServletPath()).thenReturn("/product");
        when(productDao.getOneById(12)).thenReturn(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal(45.5)));
        Map map = new HashMap();
        final Product returnedProduct = controller.getProductById(req, map);
        assertEquals(12, returnedProduct.getId());
        assertEquals("mockProduct", returnedProduct.getName());
        assertEquals("mockProductDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal(45.5), returnedProduct.getPrice());
        assertTrue(map.containsKey("product"));
        assertEquals(returnedProduct, map.get("product"));
    }


    @Test
    void givenKeyWord_whenReturnsListOfProducts_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("keyWord")).thenReturn("mockProduct");
        when(productDao.searchProducts("mockProduct")).thenReturn(Arrays.asList(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal(45.5))));
        Map map = new HashMap();
        controller.searchProductByKeyWord(req, map);
        assertTrue(map.containsKey("keyWord"));
        assertTrue(map.containsKey("productCount"));
        assertTrue(map.containsKey("productList"));
        List<Product> productList = (List<Product>) map.get("productList");
        assertEquals(1, productList.size());
        Product returnedProduct = productList.get(0);
        assertEquals("mockProduct", returnedProduct.getName());
        assertEquals("mockProductDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal(45.5), returnedProduct.getPrice());
    }


    @Test
    void givenEmptyKeyWord_whenReturnsNull_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("keyWord")).thenReturn(null);
        when(productDao.searchProducts("mockProduct")).thenReturn(Arrays.asList(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal(45.5))));
        Map map = new HashMap();
        controller.searchProductByKeyWord(req, map);
        assertFalse(map.containsKey("keyWord"));
        assertFalse(map.containsKey("productCount"));
        assertFalse(map.containsKey("productList"));
    }

    @Test
    void whenReturnsListOfProducts_thenCorrect() {
        when(productDao.getAll()).thenReturn(Arrays.asList(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal(45.5))));
        Map map = new HashMap();
        controller.getAll(map);
        assertTrue(map.containsKey("productCount"));
        assertTrue(map.containsKey("productList"));
        List<Product> productList = (List<Product>) map.get("productList");
        assertEquals(1, productList.size());
        Product returnedProduct = productList.get(0);
        assertEquals("mockProduct", returnedProduct.getName());
        assertEquals("mockProductDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal(45.5), returnedProduct.getPrice());
    }

    @Test
    void givenRequestToGetProduct_whenReturnProductId_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/product/12");
        when(req.getServletPath()).thenReturn("/product");
        int parameter = controller.getParameterFromUrl(req);
        assertEquals(12, parameter);
    }

    @Test
    void givenRequestToEditProduct_whenReturnProductId_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/product/edit/12");
        when(req.getServletPath()).thenReturn("/product/edit");
        int parameter = controller.getParameterFromUrl(req);
        assertEquals(12, parameter);
    }

    @Test
    void givenRequestAfterFormSubmission_whenGetProductDTOFromRequest_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(any())).thenReturn("12").thenReturn("mockName").thenReturn("mockDescription").thenReturn("").thenReturn("33.2");
        final ProductDTO productFromRequest = controller.getProductFromRequest(req);
        assertEquals("12", productFromRequest.getId());
        assertEquals("mockName", productFromRequest.getName());
        assertEquals("mockDescription", productFromRequest.getDescription());
        assertEquals("", productFromRequest.getPictureUrl());
        assertEquals("33.2", productFromRequest.getPrice());
    }

    @Test
    void givenRequestAfterFormSubmissionWithExistingProduct_whenUpdated_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(any())).thenReturn("12").thenReturn("mockName").thenReturn("mockDescription").thenReturn("").thenReturn("33.2");
        when(productDao.updateOne(any())).thenReturn(new Product(12, "mockName", "mockDescription", "", new BigDecimal(33.2)));

        Map map = new HashMap();
        controller.processProductFormSubmission(req, map);
        assertTrue(map.containsKey("product"));
        Product returnedProduct = (Product) map.get("product");
        assertEquals(12, returnedProduct.getId());
        assertEquals("mockName", returnedProduct.getName());
        assertEquals("mockDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal(33.2), returnedProduct.getPrice());
    }

    @Test
    void givenRequestAfterFormSubmissionWithExistingProduct_whenInserted_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(any())).thenReturn("0").thenReturn("mockName").thenReturn("mockDescription").thenReturn("").thenReturn("33.2");
        when(productDao.insertOne(any())).thenReturn(new Product(12, "mockName", "mockDescription", "", new BigDecimal(33.2)));

        Map map = new HashMap();
        controller.processProductFormSubmission(req, map);
        assertTrue(map.containsKey("product"));
        Product returnedProduct = (Product) map.get("product");
        assertEquals(12, returnedProduct.getId());
        assertEquals("mockName", returnedProduct.getName());
        assertEquals("mockDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal(33.2), returnedProduct.getPrice());
    }
}