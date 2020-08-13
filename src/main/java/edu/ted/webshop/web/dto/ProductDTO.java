package edu.ted.webshop.web.dto;

import edu.ted.webshop.entity.Product;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ProductDTO {

    private String id;
    private String name;
    private String description;
    private String pictureUrl;
    private String price;
    private String versionId;

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public ProductDTO() {
        this.id= "0";
        this.name = "";
        this.description = "";
        this.pictureUrl = "";
        this.price = "";
        this.versionId = "";
    }

    public ProductDTO(String id, String name, String description, String pictureUrl, String price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.price = price;
    }

    public ProductDTO(String id, String name, String description, String pictureUrl, String price, String versionId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.price = price;
        this.versionId = versionId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return id.equals(that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(pictureUrl, that.pictureUrl) &&
                Objects.equals(price, that.price) &&
                Objects.equals(versionId, that.versionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, pictureUrl, price, versionId);
    }
}
