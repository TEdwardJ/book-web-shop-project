package edu.ted.webshop.service;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;
import edu.ted.webshop.web.dto.ProductConverter;
import edu.ted.webshop.web.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

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
        Map<String, Object> parametersMap = new HashMap<>();
        service.validateProduct(testedProduct, parametersMap);
        assertNull(parametersMap.get("validationWarning"));
    }

    @Test
    public void givenProductWithEmptyName_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "", "description", "", "4.5");
        Map<String, Object> parametersMap = new HashMap<>();
        service.validateProduct(testedProduct, parametersMap);
        List<String> warningsList = (List<String>) parametersMap.get("validationWarning");
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Name cannot be empty;"));
    }

    @Test
    public void givenProductWithEmptyPrice_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "");
        Map<String, Object> parametersMap = new HashMap<>();
        service.validateProduct(testedProduct, parametersMap);
        List<String> warningsList = (List<String>) parametersMap.get("validationWarning");
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Price should be specified;"));
    }

    @Test
    public void givenProductWithPriceContainingLetters_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "fdf");
        Map<String, Object> parametersMap = new HashMap<>();
        service.validateProduct(testedProduct, parametersMap);
        List<String> warningsList = (List<String>) parametersMap.get("validationWarning");
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Price should not contain any characters except digits and delimiters;"));
    }

    @Test
    public void givenProductWithZeroPrice_whenValidate_thenWarning() {
        ProductDTO testedProduct = new ProductDTO("1222", "name", "description", "", "0");
        Map<String, Object> parametersMap = new HashMap<>();
        service.validateProduct(testedProduct, parametersMap);
        List<String> warningsList = (List<String>) parametersMap.get("validationWarning");
        assertEquals(1, warningsList.size());
        assertTrue(warningsList.contains("Product Price should be set to the value greater than 0;"));
    }

    @Test
    public void givenOldAndNewProductVersionsEqual_whenValidateVersionReturnsFalse_thenCorrect() {
        String oldVersion = UUID.randomUUID().toString();
        String newVersion = oldVersion;
        boolean validationResult = service.validateVersion(oldVersion, newVersion);
        assertFalse(validationResult);
    }

    @Test
    public void givenOldAndNewProductVersionsNonEqual_whenValidateVersionReturnsTrue_thenCorrect() {
        String oldVersion = UUID.randomUUID().toString();
        String newVersion = UUID.randomUUID().toString();
        boolean validationResult = service.validateVersion(oldVersion, newVersion);
        assertTrue(validationResult);
    }

    @Test
    public void givenProductId_whenReturnsProduct_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/product/12");
        when(req.getServletPath()).thenReturn("/product");
        Product product = new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal("45.5"));
        product.setVersionId(UUID.randomUUID().toString());
        when(productDao.getOneById(12)).thenReturn(product);
        Map<String, Object> map = new HashMap<>();
        Product returnedProduct = service.getProductById(req, map);
        ProductDTO returnedProductDTO = ProductConverter.fromProduct(returnedProduct);
        assertTrue(map.containsKey("formAction"));
        assertTrue(map.get("formAction").toString().startsWith("/product/edit/"));
        assertEquals(12, returnedProduct.getId());
        assertEquals("mockProduct", returnedProduct.getName());
        assertEquals("mockProductDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(new BigDecimal("45.5"), returnedProduct.getPrice());
        assertTrue(map.containsKey("product"));
        assertEquals(returnedProductDTO, map.get("product"));
    }


    @Test
    public void givenKeyWord_whenReturnsListOfProductDTO_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("keyWord")).thenReturn("mockProduct");
        when(productDao.searchProducts("mockProduct")).thenReturn(Arrays.asList(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal("45.5"))));
        Map<String, Object> map = new HashMap<>();
        service.searchProductByKeyWord(req, map);
        assertTrue(map.containsKey("keyWord"));
        assertTrue(map.containsKey("productCount"));
        assertTrue(map.containsKey("productList"));
        List<ProductDTO> productList = (List<ProductDTO>) map.get("productList");
        assertEquals(1, productList.size());
        ProductDTO returnedProduct = productList.get(0);
        assertEquals("mockProduct", returnedProduct.getName());
        assertEquals("mockProductDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals("45.5", returnedProduct.getPrice());
    }


    @Test
    public void givenEmptyKeyWord_whenReturnsNull_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("keyWord")).thenReturn(null);
        when(productDao.searchProducts("mockProduct")).thenReturn(Arrays.asList(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal("45.5"))));
        Map<String, Object> map = new HashMap<>();
        service.searchProductByKeyWord(req, map);
        assertFalse(map.containsKey("keyWord"));
        assertFalse(map.containsKey("productCount"));
        assertFalse(map.containsKey("productList"));
    }

    @Test
    public void whenReturnsListOfProductDTO_thenCorrect() {
        when(productDao.getAll()).thenReturn(Arrays.asList(new Product(12, "mockProduct", "mockProductDescription", "", new BigDecimal("45.5"))));
        Map<String, Object> map = new HashMap<>();
        service.getAll(map);
        assertTrue(map.containsKey("productCount"));
        assertTrue(map.containsKey("productList"));
        List<ProductDTO> productList = (List<ProductDTO>) map.get("productList");
        assertEquals(1, productList.size());
        ProductDTO returnedProduct = productList.get(0);
        assertEquals("mockProduct", returnedProduct.getName());
        assertEquals("mockProductDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals("45.5", returnedProduct.getPrice());
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
        when(req.getRequestURI()).thenReturn("/product/edit/12");
        ProductDTO productFromRequest = service.getProductFromRequest(req);
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
        when(req.getRequestURI()).thenReturn("/product/edit/12");
        Product mockProduct = new Product(12, "mockName", "mockDescription", "", new BigDecimal("33.2"));
        mockProduct.setVersionId("bbbb-cccc-dddd");
        when(productDao.updateOne(any())).thenReturn(mockProduct);

        Map<String, Object> map = new HashMap<>();
        service.processProductFormSubmission(req, map);
        assertTrue(map.containsKey("product"));
        assertTrue(map.containsKey("formAction"));
        assertTrue(map.get("formAction").toString().startsWith("/product/edit/"));
        ProductDTO returnedProduct = (ProductDTO) map.get("product");
        assertEquals("12", returnedProduct.getId());
        assertEquals("mockName", returnedProduct.getName());
        assertEquals("mockDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals(0, returnedProduct.getPrice().compareTo("33.2"));
        assertEquals("bbbb-cccc-dddd", returnedProduct.getVersionId());
    }

    @Test
    public void givenRequestAfterFormSubmissionWithNonExistingProduct_whenInserted_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(any())).thenReturn("0").thenReturn("mockName").thenReturn("mockDescription").thenReturn("").thenReturn("33.2").thenReturn("");
        when(req.getRequestURI()).thenReturn("/product/add");
        Product insertedProduct = new Product(12, "mockName", "mockDescription", "", new BigDecimal("33.2"));
        insertedProduct.setVersionId(UUID.randomUUID().toString());
        when(productDao.insertOne(any())).thenReturn(insertedProduct);

        Map<String, Object> map = new HashMap<>();
        service.processProductFormSubmission(req, map);
        assertTrue(map.containsKey("product"));
        assertTrue(map.containsKey("formAction"));
        assertTrue(map.get("formAction").toString().startsWith("/product/edit/"));
        ProductDTO returnedProduct = (ProductDTO) map.get("product");
        assertEquals("12", returnedProduct.getId());
        assertEquals("mockName", returnedProduct.getName());
        assertEquals("mockDescription", returnedProduct.getDescription());
        assertEquals("", returnedProduct.getPictureUrl());
        assertEquals("33.2", returnedProduct.getPrice());
    }

    @Test
    public void givenNewProductDTO_whenProcessed_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(any())).thenReturn("0").thenReturn("mockName").thenReturn("mockDescription").thenReturn("").thenReturn("33.2").thenReturn("");
        ProductDTO newProductToBeProcessed = new ProductDTO("12", "mockName", "mockDescription", "", "33.2");
        Product insertedProduct = new Product(12, "mockName", "mockDescription", "", new BigDecimal("33.2"));
        insertedProduct.setVersionId(UUID.randomUUID().toString());
        when(productDao.insertOne(any())).thenReturn(insertedProduct);

        Map<String, Object> map = new HashMap<>();
        service.processNewProduct(map, newProductToBeProcessed);
        assertTrue(map.containsKey("product"));
        assertTrue(map.containsKey("formAction"));
        assertTrue(map.get("formAction").toString().startsWith("/product/edit/"));
        ProductDTO processedProduct = (ProductDTO) map.get("product");
        assertEquals("12", processedProduct.getId());
        assertEquals("mockName", processedProduct.getName());
        assertEquals("mockDescription", processedProduct.getDescription());
        assertEquals("", processedProduct.getPictureUrl());
        assertEquals("33.2", processedProduct.getPrice());
    }

    @Test
    public void givenExistingProductDTO_whenProcessed_thenCorrect() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(any())).thenReturn("12").thenReturn("mockName").thenReturn("mockDescription").thenReturn("").thenReturn("33.2").thenReturn("BB-CC-DD");
        ProductDTO newProductToBeProcessed = new ProductDTO("12", "mockName", "mockDescription", "", "33.2", "BB-CC-DD");
        when(productDao.updateOne(any())).thenReturn(new Product(12, "mockName", "mockDescription", "", new BigDecimal("33.2")));

        Map<String, Object> map = new HashMap<>();
        service.processExistingProduct(req.getRequestURI(), map, newProductToBeProcessed);
        assertTrue(map.containsKey("product"));
        assertTrue(map.containsKey("formAction"));
        assertTrue(map.get("formAction").toString().startsWith("/product/edit/"));
        ProductDTO processedProduct = (ProductDTO) map.get("product");
        assertEquals("12", processedProduct.getId());
        assertEquals("mockName", processedProduct.getName());
        assertEquals("mockDescription", processedProduct.getDescription());
        assertEquals("", processedProduct.getPictureUrl());
        assertEquals("33.2", processedProduct.getPrice());
    }

    @Test
    public void givenNewProductDto_whenGetFormActionReturnsAddActionUrl_thenCorrect() {
        ProductDTO product = new ProductDTO("0", "", "", "", "12");
        String formAction = service.getFormActionByProduct(product);
        assertTrue(formAction.startsWith("/product/add"));
    }

    @Test
    public void givenNewProductDto_whenGetFormActionReturnsEditActionUrl_thenCorrect() {
        ProductDTO product = new ProductDTO("12", "", "", "", "12", "bb-cc-dd");
        String formAction = service.getFormActionByProduct(product);
        assertTrue(formAction.startsWith("/product/edit/12"));
    }

    @Test
    public void givenProductDtoWithNoVersion_whenMapContainsAddNewFormAction_thenCorrect(){
        ProductDTO newProductToBeProcessed = new ProductDTO("12", "mockName", "mockDescription", "", "33.2");
        Map<String, Object> map = new HashMap<>();
        service.prepareResponseMap(map, newProductToBeProcessed);
        assertEquals("/product/add", map.get("formAction"));
        assertEquals(newProductToBeProcessed, map.get("product"));
    }

    @Test
    public void givenProductDtoWithVersion_whenMapContainsAddNewFormAction_thenCorrect(){
        ProductDTO newProductToBeProcessed = new ProductDTO("122", "mockName", "mockDescription", "", "33.2", "BB-CC-DD");
        Map<String, Object> map = new HashMap<>();
        service.prepareResponseMap(map, newProductToBeProcessed);
        assertEquals("/product/edit/122", map.get("formAction"));
        assertEquals(newProductToBeProcessed, map.get("product"));
    }

}
