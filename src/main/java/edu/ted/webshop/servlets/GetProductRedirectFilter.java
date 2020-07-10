package edu.ted.webshop.servlets;

import org.eclipse.jetty.server.Request;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(servletNames = {"GetProductServlet"})
public class GetProductRedirectFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!((Request) request).getMethod().equals("GET")) {
            chain.doFilter(request, response);
        } else {
            final String requestURI = ((HttpServletRequest) request).getRequestURI();
            if ("/".equals(requestURI)) {
                request.getRequestDispatcher("/product/all").forward(request, response);
            } else if (requestURI.startsWith("/product")) {
                if (requestURI.startsWith("/product/all")) {
                    chain.doFilter(request, response);
                    return;
                } else
                if (requestURI.startsWith("/product/add")) {
                    request.getRequestDispatcher("/product/add").forward(request, response);
                } else if (requestURI.startsWith("/product/edit/")) {
                    request.getRequestDispatcher(requestURI).forward(request, response);
                } else if (!requestURI.matches("/product/[0-9]+")) {
                    ((HttpServletResponse) response).sendRedirect("/notFound.html");
                }
            }
        chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

}

