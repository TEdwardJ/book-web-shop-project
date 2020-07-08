package edu.ted.webshop.entity;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class Product {
    private int id;
    private String name;
    private String description;
    private String pictureUrl;
    private BigDecimal price = new BigDecimal(0);
    private Map<String, String> properties = new LinkedHashMap<>();

    public Product() {
    }


    public Product(String name, String description, String pictureUrl, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.price = price;
    }

    public Product(int id, String name, String description, String pictureUrl, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
