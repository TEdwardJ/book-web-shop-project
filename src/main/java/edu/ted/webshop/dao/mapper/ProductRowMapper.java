package edu.ted.webshop.dao.mapper;

import edu.ted.webshop.entity.Product;
import edu.ted.webshop.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProductRowMapper {
    private static Logger LOGGER = LoggerFactory.getLogger(ProductRowMapper.class);

    public static Product map(ResultSet result) {
        try {
            final Product product = new Product(result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getBigDecimal(5)
            );

            product.setVersionId(Optional.ofNullable(result.getString(6)).orElse(""));
            return product;
        } catch (SQLException throwables) {
            LOGGER.error("Error occured: {}", throwables);
            throw new DataException("Attempt to create Product object based on DB data failed", throwables);
        }
    }
}
