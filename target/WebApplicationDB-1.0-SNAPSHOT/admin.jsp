<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.webapplicationdb.dao.*" %>
<%@ page import="com.webapplicationdb.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - Social Media Platform</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null || (!userRole.equals("admin") && !userRole.equals("super_admin"))) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        HelpMessageDAO helpMessageDAO = new HelpMessageDAO();
        
        List<User> users = userDAO.getAllUsers();
        if (userRole.equals("super_admin")) {
            users.addAll(userDAO.getAllAdmins());
        }
        
        List<HelpMessage> messages = helpMessageDAO.getLatestMessages();
    %>
    
    <nav class="navbar">
        <div class="navbar-content">
            <h1>Admin Dashboard</h1>
            <div class="nav-links">
                <a href="create.jsp">Create User</a>
                <a href="update.jsp">Update User</a>
                <a href="delete.jsp">Delete User</a>
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

        <div style="margin: 2rem 0;">
            <h3>User List</h3>
            <div class="users-list">
            <% for (User user : users) { %>
                <div class="user-card">
                    <p><strong>Username:</strong> <%= user.getUserName() %></p>
                    <p><strong>Role:</strong> <%= user.getUserRole() %></p>
                </div>
            <% } %>
            </div>
        </div>

        <div style="margin: 2rem 0;">
            <h3>Latest Help Messages</h3>
            <div class="help-messages">
            <% if (messages.isEmpty()) { %>
                <p>No messages to display.</p>
            <% } else {
                for (HelpMessage message : messages) {
            %>
                <div class="help-message">
                    <div class="help-message-header">
                        <span>From: <%= message.getUserName() %></span>
                        <span>Time: <%= message.getCreatedAt() %></span>
                    </div>
                    <div class="post-content">
                        <%= message.getMessage() %>
                    </div>
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
