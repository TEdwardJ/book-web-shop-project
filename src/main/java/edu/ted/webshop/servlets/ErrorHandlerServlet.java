package edu.ted.webshop.servlets;

import edu.ted.webshop.dao.JdbcProductDao;
import edu.ted.webshop.utils.TemplateEngine;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@WebServlet("/errorHandler")
public class ErrorHandlerServlet extends HttpServlet {

    private TemplateEngine templateEngine = TemplateEngine.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        final RequestDispatcher requestDispatcher = req.getRequestDispatcher("/errorHandler");
        Map map = new HashMap();
        StringWriter errorWriter = new StringWriter();
        Arrays.asList(requestDispatcher.ERROR_STATUS_CODE, requestDispatcher.ERROR_EXCEPTION_TYPE, requestDispatcher.ERROR_MESSAGE)
                .forEach(e ->
                        errorWriter.write("<li>" + e + ":" + req.getAttribute(e) + " </li>")
                );
        map.put("errorDescription", errorWriter.toString());
        templateEngine.writePage("errorHandler.html", resp.getWriter(), map);
    }
}
