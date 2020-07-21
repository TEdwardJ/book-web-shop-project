package edu.ted.webshop.entity;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProductDTO {

    private String id;
    private String name;
    private String description;
    private String pictureUrl;
    private String price;

    private String versionId;

    public ProductDTO(String id, String name, String description, String pictureUrl, String price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.price = price;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Product getProduct() {
        BigDecimal productPrice = new BigDecimal(price.replace(",", "."));
        final Product product = new Product(Integer.parseInt(id), name, description, pictureUrl, productPrice);
        product.setVersionId(this.getVersionId());
        return product;
    }
}
