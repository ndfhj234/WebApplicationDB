<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.webapplicationdb.dao.*" %>
<%@ page import="com.webapplicationdb.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Delete Users - Admin Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .delete-container {
            background-color: var(--card-bg);
            border-radius: 12px;
            padding: 1.5rem;
            box-shadow: var(--shadow);
            margin: 2rem 0;
            max-height: 600px;
            overflow-y: auto;
        }
        
        .user-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px;
            border-bottom: 1px solid var(--border-color);
        }
        
        .user-item:last-child {
            border-bottom: none;
        }
        
        .user-info {
            display: flex;
            flex-direction: column;
        }
        
        .user-name {
            font-weight: bold;
            margin-bottom: 4px;
        }
        
        .user-role {
            color: #666;
            font-size: 0.9rem;
        }
        
        .actions-bar {
            margin-top: 1.5rem;
            display: flex;
            justify-content: space-between;
        }
    </style>
</head>
<body>
    <%
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null || (!userRole.equals("admin") && !userRole.equals("super_admin"))) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        if (userRole.equals("super_admin")) {
            users.addAll(userDAO.getAllAdmins());
        }
    %>
    
    <nav class="navbar">
        <div class="navbar-content">
            <h1>Admin Dashboard</h1>
            <div class="nav-links">
                <a href="admin.jsp">Dashboard</a>
                <a href="create.jsp">Create User</a>
                <a href="update.jsp">Update User</a>
                <a href="LogoutServlet">Logout</a>
            </div>
        </div>
    </nav>
    
    <div class="container">
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="message message-success">
                ${sessionScope.successMessage}
            </div>
            <% session.removeAttribute("successMessage"); %>
        </c:if>
        
        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="message message-error">
                ${sessionScope.errorMessage}
            </div>
            <% session.removeAttribute("errorMessage"); %>
        </c:if>
        
        <h2>Delete Users</h2>
        <p>Select the users you want to delete and click the "Delete Selected Users" button.</p>
        
        <form action="AdminServlet" method="post">
            <input type="hidden" name="action" value="delete">
            
            <div class="delete-container">
                <% if (users.isEmpty()) { %>
                    <p>No users found in the system.</p>
                <% } else { %>
                    <% for (User user : users) { %>
                        <div class="user-item">
                            <div class="user-info">
                                <div class="user-name"><%= user.getUserName() %></div>
                                <div class="user-role">Role: <%= user.getUserRole() %></div>
                            </div>
                            <div>
                                <input type="checkbox" name="usersToDelete" value="<%= user.getUserName() %>"
                                    <% if (user.getUserRole().equals("admin") && !userRole.equals("super_admin")) { %>
                                        disabled title="Only super_admin can delete admin users"
                                    <% } %>
                                >
                            </div>
                        </div>
                    <% } %>
                <% } %>
            </div>
            
            <div class="actions-bar">
                <a href="admin.jsp" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-danger">Delete Selected Users</button>
            </div>
        </form>
    </div>
    
    <div class="footer">
        <p>Admin Dashboard - User Management</p>
    </div>
</body>
</html>