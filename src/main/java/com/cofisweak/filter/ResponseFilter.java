package com.cofisweak.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = {"/*"})
@Log4j
public class ResponseFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Setting charsets");
        servletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
