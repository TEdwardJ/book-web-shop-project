package edu.ted.webshop.servlets;

import edu.ted.webshop.entity.Product;
import org.eclipse.jetty.server.Request;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebFilter(servletNames = {"editProductServlet"})
public class ProductValidationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    private boolean isEmptyOrNull(String field) {
        return field == null || field.isEmpty();
    }

    private List<String> validateProduct(ServletRequest req) {
        List<String> validationErrorList = new ArrayList<>();
        String productName = req.getParameter("name");
        String productDescription = req.getParameter("description");
        String pictureUrl = req.getParameter("pictureUrl");
        String price = req.getParameter("price");
        //TODO: how to correctly convert digital delimiters
        BigDecimal productPrice = new BigDecimal(price.replace(",", "."));
        if (isEmptyOrNull(productName)) {
            validationErrorList.add("Product Name cannot be empty;");
        }
        if (isEmptyOrNull(price) || productPrice.intValue() == 0) {
            validationErrorList.add("Product Price should be set to the value greater than 0;");
        }
        return validationErrorList;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (((Request) request).getMethod().equals("POST"))  {
            List<String> validationWarningList = validateProduct(request);
            if (validationWarningList.isEmpty()) {
            } else {
                request.setAttribute("validationWarning", validationWarningList);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
