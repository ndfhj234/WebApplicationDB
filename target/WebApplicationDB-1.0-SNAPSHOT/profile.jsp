<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.webapplicationdb.dao.*" %>
<%@ page import="com.webapplicationdb.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Profile - Connect</title>
    <link rel="stylesheet" href="css/style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <%
        String userName = (String) session.getAttribute("user");
        if (userName == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        PostDAO postDAO = new PostDAO();
        Post userPosts = postDAO.getUserPosts(userName);
    %>
    
    <nav class="navbar">
        <div class="navbar-content">
            <h1>Connect</h1>
            <div class="nav-links">
                <a href="landing.jsp">Home</a>
                <a href="users.jsp">Users</a>
                <a href="help.jsp">Help</a>
                <a href="LogoutServlet">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="message message-success">
                <svg viewBox="0 0 24 24" width="24" height="24" style="vertical-align: middle; margin-right: 10px;">
                    <path fill="#2e7d32" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                </svg>
                ${sessionScope.successMessage}
                <% session.removeAttribute("successMessage"); %>
            </div>
        </c:if>
        
        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="message message-error">
                <svg viewBox="0 0 24 24" width="24" height="24" style="vertical-align: middle; margin-right: 10px;">
                    <path fill="#d32f2f" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/>
                </svg>
                ${sessionScope.errorMessage}
                <% session.removeAttribute("errorMessage"); %>
            </div>
        </c:if>

        <div class="profile-header">
            <div class="profile-avatar">
                <svg viewBox="0 0 24 24" width="100%" height="100%">
                    <path fill="var(--primary-color)" d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                </svg>
            </div>
            <div class="profile-info">
                <h2><%= userName %></h2>
                <p class="profile-status">Active User</p>
            </div>
        </div>

        <div class="profile-content">
            <div class="form-container create-post-container">
                <h3>
                    <svg viewBox="0 0 24 24" width="24" height="24" style="vertical-align: middle; margin-right: 10px;">
                        <path fill="var(--primary-color)" d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/>
                    </svg>
                    Create New Post
                </h3>
                <form action="PostServlet" method="post">
                    <input type="hidden" name="action" value="create">
                    <div class="form-group">
                        <textarea name="content" class="form-control" rows="4" maxlength="200" placeholder="What's on your mind?" required></textarea>
                    </div>
                    <div class="form-group" style="text-align: right; margin-bottom: 0;">
                        <button type="submit" class="btn btn-primary">
                            <svg viewBox="0 0 24 24" width="16" height="16" style="vertical-align: middle; margin-right: 5px;">
                                <path fill="currentColor" d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
                            </svg>
                            Post
                        </button>
                    </div>
                </form>
            </div>

            <div class="content-section">
                <div class="section-header">
                    <h3>
                        <svg viewBox="0 0 24 24" width="24" height="24" style="vertical-align: middle; margin-right: 10px;">
                            <path fill="var(--primary-color)" d="M21.99 4c0-1.1-.89-2-1.99-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4-.01-18zM18 14H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
                        </svg>
                        Your Posts
                    </h3>
                </div>
                
                <% if (userPosts == null || userPosts.getNonNullPosts().isEmpty()) { %>
                    <div class="empty-state">
                        <svg viewBox="0 0 24 24" width="48" height="48">
                            <path fill="var(--text-light)" d="M21.99 4c0-1.1-.89-2-1.99-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4-.01-18zM18 14H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
                        </svg>
                        <p>No posts yet. Create your first post!</p>
                    </div>
                <% } else {
                    String[] posts = userPosts.getPosts();
                    for (int i = 0; i < posts.length; i++) {
                        if (posts[i] != null && !posts[i].trim().isEmpty()) {
                %>
                            <div class="post">
                                <div class="post-header">
                                    <span class="post-author">You</span>
                                    <form action="PostServlet" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="postIndex" value="<%= i %>">
                                        <button type="submit" class="btn btn-danger btn-sm">
                                            <svg viewBox="0 0 24 24" width="16" height="16" style="vertical-align: middle;">
                                                <path fill="currentColor" d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
                                            </svg>
                                        </button>
                                    </form>
                                </div>
                                <div class="post-content">
                                    <%= posts[i] %>
                                </div>
                            </div>
                <%
                        }
                    }
                }
                %>
            </div>
        </div>
    </div>
    
    <footer class="footer">
        <p>Oriondo | Repollo | Sarvida</p>
    </footer>
    
    <style>
        .profile-header {
            display: flex;
            align-items: center;
            margin-bottom: 2rem;
            background-color: var(--card-bg);
            border-radius: 12px;
            box-shadow: var(--shadow);
            padding: 2rem;
        }
        
        .profile-avatar {
            width: 120px;
            height: 120px;
            background-color: rgba(74, 118, 168, 0.1);
            border-radius: 50%;
            margin-right: 2rem;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 3px solid var(--primary-light);
            overflow: hidden;
        }
        
        .profile-info h2 {
            font-size: 1.8rem;
            margin-bottom: 0.5rem;
        }
        
        .profile-status {
            color: var(--text-light);
            font-size: 1rem;
        }
        
        .profile-content {
            display: flex;
            flex-direction: column;
            gap: 2rem;
        }
        
        .create-post-container {
            width: 100%;
            max-width: 100%;
        }
        
        .create-post-container h3 {
            display: flex;
            align-items: center;
            margin-bottom: 1.5rem;
        }
        
        .section-header {
            margin-bottom: 1.5rem;
        }
        
        .section-header h3 {
            display: flex;
            align-items: center;
            font-size: 1.5rem;
        }
        
        .empty-state {
            background-color: var(--card-bg);
            border-radius: 12px;
            box-shadow: var(--shadow);
            padding: 3rem 2rem;
            text-align: center;
            color: var(--text-light);
        }
        
        .empty-state svg {
            margin-bottom: 1rem;
        }
        
        .empty-state p {
            margin-bottom: 1.5rem;
            font-size: 1.1rem;
        }
        
        .content-section {
            margin: 2rem 0;
        }
        
        @media (max-width: 768px) {
            .profile-header {
                flex-direction: column;
                text-align: center;
                padding: 1.5rem;
            }
            
            .profile-avatar {
                margin-right: 0;
                margin-bottom: 1.5rem;
            }
            
            .create-post-container {
                padding: 1.5rem;
            }
        }
    </style>
</body>
</html>
