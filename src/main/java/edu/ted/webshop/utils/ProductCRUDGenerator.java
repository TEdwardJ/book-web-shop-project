package edu.ted.webshop.utils;

import edu.ted.webshop.entity.Product;

public class ProductCRUDGenerator {

    static String quoteReplace(String text) {
        return text.replaceAll("'", "''");
    }

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
                "INTO webshop.ws_products" +
                "(product_id, product_name, product_description, product_picture_url, product_price, product_version_id) " +
                "VALUES (" +
                "DEFAULT, " +
                "'" + quoteReplace(product.getName()) + "', " +
                "'" + quoteReplace(product.getDescription()) + "', " +
                "'" + quoteReplace(product.getPictureUrl()) + "', " +
                product.getPrice() +
                ", " +
                "'" + quoteReplace(product.getVersionId()) + "')";
        return query;
    }

    public static String getUpdateQuery(Product product, String oldVersionId) {
        String preparedOldVersionId = quoteReplace(oldVersionId);
        String query = "UPDATE " +
                "webshop.ws_products " +
                "SET " +
                "product_name = " +
                "'" + quoteReplace(product.getName()) + "', " +
                "product_description = " +
                "'" + quoteReplace(product.getDescription()) + "', " +
                "product_picture_url = " +
                "'" + quoteReplace(product.getPictureUrl()) + "'," +
                "product_price = " +
                product.getPrice() +
                ", " +
                "product_version_id = " +
                "'" + quoteReplace(product.getVersionId()) + "' " +
                "WHERE " +
                "product_id = " +
                product.getId() +
                " AND COALESCE(product_version_id, COALESCE('" + preparedOldVersionId + "','0000')) = COALESCE('" + preparedOldVersionId + "','0000')";
        return query;
    }
}
