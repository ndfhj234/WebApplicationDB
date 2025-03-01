<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.webapplicationdb.dao.*" %>
<%@ page import="com.webapplicationdb.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Users</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%
        String userName = (String) session.getAttribute("user");
        if (userName == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        FollowDAO followDAO = new FollowDAO();
        UserDAO userDAO = new UserDAO();
        
        Follow userFollows = followDAO.getUserFollows(userName);
        List<User> allUsers = userDAO.getAllUsers();
    %>
    
    <nav class="navbar">
        <div class="navbar-content">
            <h1>Connect</h1>
            <div class="nav-links">
                <a href="landing.jsp">Home</a>
                <a href="profile.jsp">Profile</a>
                <a href="help.jsp">Help</a>
                <a href="LogoutServlet">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="message message-success">
                ${sessionScope.successMessage}
            </div>
        </c:if>
        
        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="message message-error">
                ${sessionScope.errorMessage}
            </div>
        </c:if>

        <div class="form-container">
            <h3>Follow a User</h3>
            <form action="FollowServlet" method="post">
                <input type="hidden" name="action" value="follow">
                <div class="form-group">
                    <input type="text" name="userToFollow" class="form-control" placeholder="Enter username to follow" required>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Follow</button>
                </div>
            </form>
        </div>

        <div style="margin: 2rem 0;">
            <h3>Users You Follow</h3>
            
            <% if (userFollows == null || userFollows.getNonNullFollows().isEmpty()) { %>
                <p>You're not following anyone yet.</p>
            <% } else { %>
                <div class="users-list">
                <% for (String followedUser : userFollows.getNonNullFollows()) { %>
                    <div class="user-card">
                        <div style="display: flex; justify-content: space-between; align-items: center;">
                            <span><%= followedUser %></span>
                            <form action="FollowServlet" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="unfollow">
                                <input type="hidden" name="userToUnfollow" value="<%= followedUser %>">
                                <button type="submit" class="btn btn-danger" style="padding: 0.25rem 0.5rem;">Unfollow</button>
                            </form>
                        </div>
                    </div>
                <% } %>
                </div>
            <% } %>
        </div>

        <div style="margin: 2rem 0;">
            <h3>All Users</h3>
            <div class="users-list">
            <% for (User user : allUsers) {
                if (!user.getUserName().equals(userName)) {
            %>
                <div class="user-card">
                    <span><%= user.getUserName() %></span>
                </div>
            <%
                }
            }
            %>
            </div>
        </div>
    </div>
</body>
</html>
