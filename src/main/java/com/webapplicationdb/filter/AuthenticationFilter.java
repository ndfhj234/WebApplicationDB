package com.webapplicationdb.filter;

import com.webapplicationdb.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private static final List<String> PUBLIC_PATHS = Arrays.asList("/login.jsp", "/signup.jsp", "/LoginServlet", "/SignupServlet");
    private static final List<String> ADMIN_PATHS = Arrays.asList("/admin.jsp", "/create.jsp", "/update.jsp", "/delete.jsp", "/result.jsp");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getServletPath();
        String userRole = SessionUtil.getUserRole(httpRequest);
        
        // Allow access to public paths
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        if (SessionUtil.getUser(httpRequest) == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        // Check admin access
        if (isAdminPath(path)) {
            if (!"admin".equals(userRole) && !"super_admin".equals(userRole)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/landing.jsp");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::equals) || path.contains("css") || path.contains("js");
    }

    private boolean isAdminPath(String path) {
        return ADMIN_PATHS.stream().anyMatch(path::equals);
    }
}
