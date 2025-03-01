package com.webapplicationdb.servlet;

import com.webapplicationdb.dao.FollowDAO;
import com.webapplicationdb.dao.PostDAO;
import com.webapplicationdb.dao.UserDAO;
import com.webapplicationdb.model.User;
import com.webapplicationdb.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
    private UserDAO userDAO;
    private PostDAO postDAO;
    private FollowDAO followDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        postDAO = new PostDAO();
        followDAO = new FollowDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (userName == null || password == null || confirmPassword == null ||
            userName.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            SessionUtil.setErrorMessage(request, "All fields are required");
            response.sendRedirect("signup.jsp");
            return;
        }

        if (!password.equals(confirmPassword)) {
            SessionUtil.setErrorMessage(request, "Passwords do not match");
            response.sendRedirect("signup.jsp");
            return;
        }

        if (!userDAO.isUserNameAvailable(userName)) {
            SessionUtil.setErrorMessage(request, "Username is already taken");
            response.sendRedirect("signup.jsp");
            return;
        }

        User user = new User(userName, password, "user");
        if (userDAO.createUser(user)) {
            postDAO.createUserPosts(userName);
            followDAO.createUserFollows(userName);
            
            SessionUtil.setSuccessMessage(request, "Account created successfully! Please log in.");
            response.sendRedirect("login.jsp");
        } else {
            SessionUtil.setErrorMessage(request, "Error creating account");
            response.sendRedirect("signup.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("signup.jsp");
    }
}
