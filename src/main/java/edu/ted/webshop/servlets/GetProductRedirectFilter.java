package edu.ted.webshop.servlets;

import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(servletNames = {"GetProductServlet"})
public class GetProductRedirectFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("Filter Request with method {}, url {}", ((Request) request).getMethod(), ((Request) request).getRequestURI());
        if (((Request) request).getMethod().equals("GET")) {
            final String requestURI = ((HttpServletRequest) request).getRequestURI();
            if (requestURI.startsWith("/product")) {
                /*if (requestURI.startsWith("/product/edit/")) {
                    chain.doFilter(request, response);
                    return;
                } else if (!requestURI.matches("/product/[0-9]+")) {
                    ((HttpServletResponse) response).sendRedirect("/notFound.html");
                }*/
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}

