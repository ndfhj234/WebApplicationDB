package com.webapplicationdb.servlet;

import com.webapplicationdb.dao.FollowDAO;
import com.webapplicationdb.dao.UserDAO;
import com.webapplicationdb.model.Follow;
import com.webapplicationdb.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/FollowServlet")
public class FollowServlet extends HttpServlet {
    private FollowDAO followDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        followDAO = new FollowDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String userName = SessionUtil.getUser(request);

        if (userName == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if ("follow".equals(action)) {
            followUser(request, response, userName);
        } else if ("unfollow".equals(action)) {
            unfollowUser(request, response, userName);
        }
    }

    private void followUser(HttpServletRequest request, HttpServletResponse response, String userName)
            throws IOException {
        String userToFollow = request.getParameter("userToFollow");
        
        if (userToFollow == null || userToFollow.trim().isEmpty()) {
            SessionUtil.setErrorMessage(request, "Username to follow cannot be empty");
            response.sendRedirect("users.jsp");
            return;
        }

        if (userName.equals(userToFollow)) {
            SessionUtil.setErrorMessage(request, "You cannot follow yourself");
            response.sendRedirect("users.jsp");
            return;
        }

        if (userDAO.getUser(userToFollow) == null) {
            SessionUtil.setErrorMessage(request, "User does not exist");
            response.sendRedirect("users.jsp");
            return;
        }

        Follow userFollows = followDAO.getUserFollows(userName);
        if (userFollows == null) {
            userFollows = new Follow(userName);
        }

        if (userFollows.isFollowing(userToFollow)) {
            SessionUtil.setErrorMessage(request, "You are already following this user");
            response.sendRedirect("users.jsp");
            return;
        }

        if (!userFollows.hasSpace()) {
            SessionUtil.setErrorMessage(request, "You can only follow up to three users");
            response.sendRedirect("users.jsp");
            return;
        }

        if (userFollows.addFollow(userToFollow) && followDAO.updateFollows(userFollows)) {
            SessionUtil.setSuccessMessage(request, "Successfully followed " + userToFollow);
        } else {
            SessionUtil.setErrorMessage(request, "Error following user");
        }
        
        response.sendRedirect("users.jsp");
    }

    private void unfollowUser(HttpServletRequest request, HttpServletResponse response, String userName)
            throws IOException {
        String userToUnfollow = request.getParameter("userToUnfollow");
        
        Follow userFollows = followDAO.getUserFollows(userName);
        if (userFollows != null) {
            if (userFollows.unfollow(userToUnfollow) && followDAO.updateFollows(userFollows)) {
                SessionUtil.setSuccessMessage(request, "Successfully unfollowed " + userToUnfollow);
            } else {
                SessionUtil.setErrorMessage(request, "Error unfollowing user");
            }
        }
        
        response.sendRedirect("users.jsp");
    }
}
