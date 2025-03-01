<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.webapplicationdb.dao.*" %>
<%@ page import="com.webapplicationdb.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Home - Connect</title>
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
        
        FollowDAO followDAO = new FollowDAO();
        PostDAO postDAO = new PostDAO();
        
        List<String> followedUsers = followDAO.getFollowedUsers(userName);
        List<Post> followedPosts = postDAO.getFollowedUsersPosts(followedUsers);
    %>
    
    <nav class="navbar">
        <div class="navbar-content">
            <h1>Connect</h1>
            <div class="nav-links">
                <a href="profile.jsp">Profile</a>
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

        <div class="user-welcome">
            <h2>Welcome, <span class="user-name"><%= userName %></span>!</h2>
        </div>
        
        <div class="content-section">
            <div class="section-header">
                <h3>
                    <svg viewBox="0 0 24 24" width="24" height="24" style="vertical-align: middle; margin-right: 10px;">
                        <path fill="var(--primary-color)" d="M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z"/>
                    </svg>
                    Posts from People You Follow
                </h3>
            </div>
            
            <% if (followedPosts.isEmpty()) { %>
                <div class="empty-state">
                    <svg viewBox="0 0 24 24" width="48" height="48">
                        <path fill="var(--text-light)" d="M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z"/>
                    </svg>
                    <p>No posts to show. Start following some users!</p>
                    <a href="users.jsp" class="btn btn-primary">Find Users to Follow</a>
                </div>
            <% } else {
                for (Post post : followedPosts) {
                    List<String> nonNullPosts = post.getNonNullPosts();
                    for (String content : nonNullPosts) {
            %>
                        <div class="post">
                            <div class="post-header">
                                <span class="post-author"><%= post.getUserName() %></span>
                            </div>
                            <div class="post-content">
                                <%= content %>
                            </div>
                        </div>
            <%
                    }
                }
            }
            %>
        </div>
    </div>
    
    <footer class="footer">
        <p>Oriondo | Repollo | Sarvida</p>
    </footer>
    
    <style>
        .user-welcome {
            margin-bottom: 2rem;
        }
        
        .user-name {
            color: var(--primary-color);
            font-weight: 600;
        }
        
        .content-section {
            margin: 2rem 0;
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
    </style>
</body>
</html>
