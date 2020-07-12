package edu.ted.webshop.utils;

import edu.ted.webshop.entity.Product;
import edu.ted.webshop.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper {
    private static Logger logger = LoggerFactory.getLogger(ProductRowMapper.class);

    public static Product map(ResultSet result) {
        try {
            return new Product(result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getBigDecimal(5)
            );
        } catch (SQLException throwables) {
            logger.error("Error occured: {}", throwables);
            throw new DataException("Attempt to create Product object based on DB data failed", throwables);
        }
    }
}
