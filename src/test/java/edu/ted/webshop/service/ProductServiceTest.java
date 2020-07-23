package edu.ted.webshop.service;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.web.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private JdbcProductDao productDao;
    private ProductService service;

    @BeforeEach
    public void init() {
        productDao = mock(JdbcProductDao.class);
        service = new ProductService(productDao);
    }

    @Test
    public void givenProductWithAllFieldsFilled_whenValidate_thenCorrect() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "4.5");
        List<String> warningsList = service.validateProduct(testedProduct);
        assertEquals(0, warningsList.size());
    }

    @Test
    public void givenProductWithEmptyName_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "", "description", "", "4.5");
        List<String> warningsList = service.validateProduct(testedProduct);
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Name cannot be empty;"));
    }

    @Test
    public void givenProductWithEmptyPrice_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "");
        List<String> warningsList = service.validateProduct(testedProduct);
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Price should be specified;"));
    }

    @Test
    public void givenProductWithPriceContainingLetters_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "fdf");
        List<String> warningsList = service.validateProduct(testedProduct);
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Price should not contains any characters except digits and delimiters;"));
    }

    @Test
    public void givenProductWithZeroPrice_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "0");
        List<String> warningsList = service.validateProduct(testedProduct);
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Price should be set to the value greater than 0;"));
    }

    @Test
    public void givenProductId_whenReturnsProduct_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/product/12");
        when(req.getServletPath()).thenReturn("/product");
        when(productDao.getOneById(12)).thenReturn(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal(45.5)));
        Map<String, Object> map = new HashMap<>();
        final Product returnedProduct = service.getProductById(req, map);
        assertTrue(map.containsKey("formAction"));
        assertTrue(map.get("formAction").toString().startsWith("/product/edit/"));
        assertEquals(12, returnedProduct.getId());
        assertEquals("mockProduct", returnedProduct.getName());
        assertEquals("mockProductDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal("45.5"), returnedProduct.getPrice());
        assertTrue(map.containsKey("product"));
        assertEquals(returnedProduct, map.get("product"));
    }


    @Test
    public void givenKeyWord_whenReturnsListOfProducts_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("keyWord")).thenReturn("mockProduct");
        when(productDao.searchProducts("mockProduct")).thenReturn(Arrays.asList(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal(45.5))));
        Map<String, Object> map = new HashMap<>();
        service.searchProductByKeyWord(req, map);
        assertTrue(map.containsKey("keyWord"));
        assertTrue(map.containsKey("productCount"));
        assertTrue(map.containsKey("productList"));
        List<Product> productList = (List<Product>) map.get("productList");
        assertEquals(1, productList.size());
        Product returnedProduct = productList.get(0);
        assertEquals("mockProduct", returnedProduct.getName());
        assertEquals("mockProductDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal("45.5"), returnedProduct.getPrice());
    }


    @Test
    public void givenEmptyKeyWord_whenReturnsNull_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("keyWord")).thenReturn(null);
        when(productDao.searchProducts("mockProduct")).thenReturn(Arrays.asList(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal(45.5))));
        Map<String, Object> map = new HashMap<>();
        service.searchProductByKeyWord(req, map);
        assertFalse(map.containsKey("keyWord"));
        assertFalse(map.containsKey("productCount"));
        assertFalse(map.containsKey("productList"));
    }

    @Test
    public void whenReturnsListOfProducts_thenCorrect() {
        when(productDao.getAll()).thenReturn(Arrays.asList(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal(45.5))));
        Map<String, Object> map = new HashMap<>();
        service.getAll(map);
        assertTrue(map.containsKey("productCount"));
        assertTrue(map.containsKey("productList"));
        List<Product> productList = (List<Product>) map.get("productList");
        assertEquals(1, productList.size());
        Product returnedProduct = productList.get(0);
        assertEquals("mockProduct", returnedProduct.getName());
        assertEquals("mockProductDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal("45.5"), returnedProduct.getPrice());
    }

    @Test
    public void givenRequestToGetProduct_whenReturnProductId_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/product/12");
        when(req.getServletPath()).thenReturn("/product");
        int parameter = service.getParameterFromUrl(req);
        assertEquals(12, parameter);
    }

    @Test
    public void givenRequestToEditProduct_whenReturnProductId_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/product/edit/12");
        when(req.getServletPath()).thenReturn("/product/edit");
        int parameter = service.getParameterFromUrl(req);
        assertEquals(12, parameter);
    }

    @Test
    public void givenRequestAfterFormSubmission_whenGetProductDTOFromRequest_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(any())).thenReturn("12").thenReturn("mockName").thenReturn("mockDescription").thenReturn("").thenReturn("33.2").thenReturn("aaaa-bbbb-cccc");
        final ProductDTO productFromRequest = service.getProductFromRequest(req);
        assertEquals("12", productFromRequest.getId());
        assertEquals("mockName", productFromRequest.getName());
        assertEquals("mockDescription", productFromRequest.getDescription());
        assertEquals("", productFromRequest.getPictureUrl());
        assertEquals("33.2", productFromRequest.getPrice());
    }

    @Test
    public void givenRequestAfterFormSubmissionWithExistingProduct_whenUpdated_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(any())).thenReturn("12").thenReturn("mockName").thenReturn("mockDescription").thenReturn("").thenReturn("33.2").thenReturn("bbbb-cccc-dddd");
        final Product mockProduct = new Product(12, "mockName", "mockDescription", "", new BigDecimal("33.2"));
        mockProduct.setVersionId("bbbb-cccc-dddd");
        when(productDao.updateOne(any())).thenReturn(mockProduct);

        Map<String, Object> map = new HashMap<>();
        service.processProductFormSubmission(req, map);
        assertTrue(map.containsKey("product"));
        assertTrue(map.containsKey("formAction"));
        assertTrue(map.get("formAction").toString().startsWith("/product/edit/"));
        Product returnedProduct = (Product) map.get("product");
        assertEquals(12, returnedProduct.getId());
        assertEquals("mockName", returnedProduct.getName());
        assertEquals("mockDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(0, returnedProduct.getPrice().compareTo(new BigDecimal("33.2")));
        assertEquals("bbbb-cccc-dddd", returnedProduct.getVersionId());
    }

    @Test
    public void givenRequestAfterFormSubmissionWithNonExistingProduct_whenInserted_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(any())).thenReturn("0").thenReturn("mockName").thenReturn("mockDescription").thenReturn("").thenReturn("33.2");
        when(productDao.insertOne(any())).thenReturn(new Product(12, "mockName", "mockDescription", "", new BigDecimal("33.2")));

        Map<String, Object> map = new HashMap<>();
        service.processProductFormSubmission(req, map);
        assertTrue(map.containsKey("product"));
        assertTrue(map.containsKey("formAction"));
        assertTrue(map.get("formAction").toString().startsWith("/product/edit/"));
        Product returnedProduct = (Product) map.get("product");
        assertEquals(12, returnedProduct.getId());
        assertEquals("mockName", returnedProduct.getName());
        assertEquals("mockDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal("33.2"), returnedProduct.getPrice());
    }

    @Test
    public void givenNewProductDto_whenGetFormActionReturnsAddActionUrl_thenCorrect(){
        ProductDTO product = new ProductDTO("0", "", "", "", "12");
        String formAction = service.getFormAction(product);
        assertTrue(formAction.startsWith("/product/add"));
    }

    @Test
    public void givenNewProductDto_whenGetFormActionReturnsEditActionUrl_thenCorrect(){
        ProductDTO product = new ProductDTO("12", "", "", "", "12");
        String formAction = service.getFormAction(product);
        assertTrue(formAction.startsWith("/product/edit/12"));
    }

    @Test
    public void givenNewProduct_whenGetFormActionReturnsAddActionUrl_thenCorrect(){
        Product product = new Product();
        String formAction = service.getFormAction(product);
        assertTrue(formAction.startsWith("/product/add"));
    }

    @Test
    public void givenNewProduct_whenGetFormActionReturnsEditActionUrl_thenCorrect(){
        Product product = new Product();
        product.setId(12);
        String formAction = service.getFormAction(product);
        assertTrue(formAction.startsWith("/product/edit/12"));
    }
}