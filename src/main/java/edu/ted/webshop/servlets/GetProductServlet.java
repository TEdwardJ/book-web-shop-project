package edu.ted.webshop.servlets;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.entity.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "GetProductServlet", urlPatterns = "/product/*")
public class GetProductServlet extends HttpServlet {


    private JdbcProductDao productDao = new JdbcProductDao();
    private final TemplateEngine templateEngine = TemplateEngine.getInstance();

    public GetProductServlet() throws IOException  {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        Map map = new HashMap();
        int productId  = getParameterFromUrl(req);
        Product product = productDao.getOneById(productId);
        map.put("product", product);
        templateEngine.writePage("product.html",resp.getWriter(),map);
    }

    private int getParameterFromUrl(HttpServletRequest req) {
        String servletPath = req.getServletPath();
        String requestURI = req.getRequestURI();
        Pattern pattern = Pattern.compile(".*"+servletPath+"/(.*)");
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
}
