package edu.ted.webshop.dao;

import edu.ted.webshop.entity.Product;

public class ProductCRUDGenerator {


    public static String getSelectAllQuery() {
        return "SELECT product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id FROM webshop.ws_products";
    }

    public static String getFindAllQuery(String keyWord) {
        String query = "SELECT " +
                "product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id " +
                "FROM " +
                "webshop.ws_products " +
                "WHERE lower(concat(product_name, product_description)) " +
                "like lower(concat('%','" + keyWord + "','%'))";
        return query;
    }

    public static String getGetOneQuery(int id) {
        String query = "SELECT " +
                "product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id " +
                "FROM " +
                "webshop.ws_products " +
                "WHERE " +
                "product_id = " +
                id;
        return query;
    }

    public static String getInsertQuery(Product product) {
        String query = "INSERT " +
                "into webshop.ws_products" +
                "(product_id, product_name, product_description, product_picture_url, product_price, product_version_id) " +
                "VALUES (" +
                "DEFAULT, " +
                "'" + product.getName() + "', " +
                "'" + product.getDescription() + "', " +
                "'" + product.getPictureUrl() + "'," +
                product.getPrice() +
                ", " +
                "'" + product.getVersionId() + "')";
        return query;
    }

    public static String getUpdateQuery(Product product, String oldVersionId) {
        String query = "UPDATE " +
                "webshop.ws_products " +
                "SET " +
                "product_name = " +
                "'" + product.getName() + "', " +
                "product_description = " +
                "'" + product.getDescription() + "', " +
                "product_picture_url = " +
                "'" + product.getPictureUrl() + "'," +
                "product_price = " +
                product.getPrice() +
                ", " +
                "product_version_id = " +
                "'" + product.getVersionId() + "' " +
                "WHERE " +
                "product_id = " +
                product.getId() +
                " AND COALESCE(product_version_id, COALESCE('" + oldVersionId + "','0000')) = COALESCE('" + oldVersionId + "','0000')";
        return query;

    }
}
