<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.webapplicationdb.dao.*" %>
<%@ page import="com.webapplicationdb.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bulk Update Users - Admin Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .bulk-edit-container {
            display: flex;
            gap: 2rem;
            margin: 2rem 0;
        }

        .user-list {
            flex: 1;
            background-color: var(--card-bg);
            border-radius: 12px;
            padding: 1.5rem;
            box-shadow: var(--shadow);
            max-height: 800px;
            overflow-y: auto;
        }

        .edit-form {
            flex: 1;
            background-color: var(--card-bg);
            border-radius: 12px;
            padding: 1.5rem;
            box-shadow: var(--shadow);
            max-height: 800px;
            overflow-y: auto;
        }

        .user-item {
            padding: 10px;
            border-bottom: 1px solid var(--border-color);
        }

        .user-edit-row {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr 1fr;
            gap: 10px;
            padding: 12px;
            align-items: center;
            border-bottom: 1px solid var(--border-color);
        }

        .user-edit-row:hover {
            background-color: rgba(74, 118, 168, 0.1);
        }

        .user-edit-row input, .user-edit-row select {
            padding: 8px;
            border: 1px solid var(--border-color);
            border-radius: 4px;
            width: 100%;
        }

        .form-header {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr 1fr;
            gap: 10px;
            padding: 12px;
            font-weight: bold;
            background-color: var(--primary-color);
            color: white;
            border-radius: 8px 8px 0 0;
        }

        @media (max-width: 768px) {
            .bulk-edit-container {
                flex-direction: column;
            }
            
            .user-edit-row, .form-header {
                grid-template-columns: 1fr;
            }
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
        
        request.setAttribute("users", users);
    %>
    
    <nav class="navbar">
        <div class="navbar-content">
            <h1>Admin Dashboard</h1>
            <div class="nav-links">
                <a href="admin.jsp">Dashboard</a>
                <a href="create.jsp">Create User</a>
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
        
        <h2>Bulk Update Users</h2>
        <p>Edit multiple users at once. Only filled fields will be updated.</p>
        
        <form action="AdminServlet" method="post">
            <input type="hidden" name="action" value="bulkUpdate">
            
            
            <div class="bulk-edit-container">
                <div class="edit-form">
                    <div class="form-header">
                        <div>Current Username</div>
                        <div>New Username</div>
                        <div>New Password</div>
                        <div>New Role</div>
                    </div>
                    
                    <c:forEach var="user" items="${users}" varStatus="status">
                        <div class="user-edit-row">
                            <div>
                                <strong>${user.getUserName()}</strong>
                                <input type="hidden" name="originalUsername_${status.index}" value="${user.getUserName()}">
                            </div>
                            <div>
                                <input type="text" name="newUsername_${status.index}" placeholder="Leave empty to keep">
                            </div>
                            <div>
                                <input type="password" name="newPassword_${status.index}" placeholder="Leave empty to keep">
                            </div>
                            <div>
                                <select name="newRole_${status.index}">
                                    <option value="">Keep current role</option>
                                    <option value="user">User</option>
                                    <c:if test="${sessionScope.userRole eq 'super_admin'}">
                                        <option value="admin">Admin</option>
                                    </c:if>
                                </select>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            
            <input type="hidden" name="userCount" value="${users.size()}">
            
            <div style="text-align: center; margin-top: 20px;">
                <button type="submit" class="btn btn-primary">Save All Changes</button>
                <a href="admin.jsp" class="btn btn-secondary" style="margin-left: 10px;">Cancel</a>
            </div>
        </form>
    </div>
    
    <div class="footer">
        <p>Admin Dashboard - Bulk User Management</p>
    </div>
</body>
</html>