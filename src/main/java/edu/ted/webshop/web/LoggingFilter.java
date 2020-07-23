package edu.ted.webshop.web;

import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class LoggingFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("Filter Request with method {}, url {}", ((Request) request).getMethod(), ((Request) request).getRequestURI());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}

