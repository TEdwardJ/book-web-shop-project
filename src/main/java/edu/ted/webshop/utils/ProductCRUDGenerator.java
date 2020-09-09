package edu.ted.webshop.utils;

import edu.ted.webshop.entity.Product;

public class ProductCRUDGenerator {

    static String getFieldValue(String fieldValue) {
        if (fieldValue == null){
            return "";
        }
        return replaceQuote(fieldValue);
    }

    static String replaceQuote(String text) {
        return text.replaceAll("'", "''");
    }

    public static String getSelectAllQuery() {
        return "SELECT product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id FROM webshop.ws_products";
    }

    public static String getFindAllQuery(String keyWord) {
        return "SELECT " +
                "product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id " +
                "FROM " +
                "webshop.ws_products " +
                "WHERE lower(concat(product_name, product_description)) " +
                "like lower(concat('%','" + keyWord + "','%'))";
    }

    public static String getGetOneQuery(int id) {
        return "SELECT " +
                "product_id, product_name, product_description, product_picture_url, product_price, creation_date, product_version_id " +
                "FROM " +
                "webshop.ws_products " +
                "WHERE " +
                "product_id = " +
                id;
    }

    public static String getInsertQuery(Product product) {
        String query = "INSERT " +
                "INTO webshop.ws_products" +
                "(product_id, product_name, product_description, product_picture_url, product_price, product_version_id) " +
                "VALUES (" +
                "DEFAULT, " +
                "'" + getFieldValue(product.getName()) + "', " +
                "'" + getFieldValue(product.getDescription()) + "', " +
                "'" + getFieldValue(product.getPictureUrl()) + "', " +
                product.getPrice() + ", " +
                "'" + getFieldValue(product.getVersionId()) + "')";
        return query;
    }

    public static String getUpdateQuery(Product product, String oldVersionId) {
        String preparedOldVersionId = getFieldValue(oldVersionId);
        String query = "UPDATE " +
                "webshop.ws_products " +
                "SET " +
                "product_name = " +
                "'" + getFieldValue(product.getName()) + "', " +
                "product_description = " +
                "'" + getFieldValue(product.getDescription()) + "', " +
                "product_picture_url = " +
                "'" + getFieldValue(product.getPictureUrl()) + "'," +
                "product_price = " +
                product.getPrice() +
                ", " +
                "product_version_id = " +
                "'" + getFieldValue(product.getVersionId()) + "' " +
                "WHERE " +
                "product_id = " +
                product.getId() +
                " AND COALESCE(product_version_id, COALESCE('" + preparedOldVersionId + "','0000')) = COALESCE('" + preparedOldVersionId + "','0000')";
        return query;
    }
}
