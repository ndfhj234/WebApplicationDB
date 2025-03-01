package com.webapplicationdb.servlet;

import com.webapplicationdb.dao.PostDAO;
import com.webapplicationdb.model.Post;
import com.webapplicationdb.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/PostServlet")
public class PostServlet extends HttpServlet {
    private PostDAO postDAO;

    @Override
    public void init() throws ServletException {
        postDAO = new PostDAO();
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

        if ("create".equals(action)) {
            createPost(request, response, userName);
        } else if ("delete".equals(action)) {
            deletePost(request, response, userName);
        }
    }

    private void createPost(HttpServletRequest request, HttpServletResponse response, String userName)
            throws IOException {
        String content = request.getParameter("content");
        
        if (content == null || content.trim().isEmpty()) {
            SessionUtil.setErrorMessage(request, "Post content cannot be empty");
            response.sendRedirect("profile.jsp");
            return;
        }

        if (content.length() > 200) {
            SessionUtil.setErrorMessage(request, "Post content cannot exceed 200 characters");
            response.sendRedirect("profile.jsp");
            return;
        }

        Post userPosts = postDAO.getUserPosts(userName);
        if (userPosts == null) {
            userPosts = new Post(userName);
        }
        
        userPosts.addPost(content);
        
        if (postDAO.updatePosts(userPosts)) {
            SessionUtil.setSuccessMessage(request, "Post created successfully");
        } else {
            SessionUtil.setErrorMessage(request, "Error creating post");
        }
        
        response.sendRedirect("profile.jsp");
    }

    private void deletePost(HttpServletRequest request, HttpServletResponse response, String userName)
            throws IOException {
        String postIndexStr = request.getParameter("postIndex");
        
        try {
            int postIndex = Integer.parseInt(postIndexStr);
            Post userPosts = postDAO.getUserPosts(userName);
            
            if (userPosts != null) {
                userPosts.deletePost(postIndex);
                
                if (postDAO.updatePosts(userPosts)) {
                    SessionUtil.setSuccessMessage(request, "Post deleted successfully");
                } else {
                    SessionUtil.setErrorMessage(request, "Error deleting post");
                }
            }
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(request, "Invalid post index");
        }
        
        response.sendRedirect("profile.jsp");
    }
}
