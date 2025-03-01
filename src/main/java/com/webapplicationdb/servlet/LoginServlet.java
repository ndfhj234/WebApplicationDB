package com.webapplicationdb.servlet;

import com.webapplicationdb.dao.UserDAO;
import com.webapplicationdb.model.User;
import com.webapplicationdb.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        if (userName == null || password == null || userName.trim().isEmpty() || password.trim().isEmpty()) {
            SessionUtil.setErrorMessage(request, "Username and password are required");
            response.sendRedirect("login.jsp");
            return;
        }

        if (userDAO.validateLogin(userName, password)) {
            User user = userDAO.getUser(userName);
            SessionUtil.setUser(request, userName, user.getUserRole());

            if ("admin".equals(user.getUserRole()) || "super_admin".equals(user.getUserRole())) {
                response.sendRedirect("admin.jsp");
            } else {
                response.sendRedirect("landing.jsp");
            }
        } else {
            SessionUtil.setErrorMessage(request, "Invalid username or password");
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
