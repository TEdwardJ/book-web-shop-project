package edu.ted.webshop.web.dto;

import edu.ted.webshop.entity.Product;

import java.math.BigDecimal;
import java.util.Optional;

public class ProductConverter {
    public static Product toProduct(ProductDTO product) {
        if (product == null) {
            return null;
        }
        final BigDecimal productPrice = new BigDecimal(product.getPrice());
        final Product newProduct = new Product(Integer.parseInt(product.getId()), product.getName(), product.getDescription(), product.getPictureUrl(), productPrice);

        newProduct.setVersionId(Optional.ofNullable(product.getVersionId()).orElse(""));
        return newProduct;
    }

    public static ProductDTO fromProduct(Product product) {
        if (product == null) {
            return new ProductDTO();
        }
        return new ProductDTO(Integer.toString(product.getId()), product.getName(), product.getDescription(), product.getPictureUrl(), product.getPrice().toString(), product.getVersionId());
    }
}
