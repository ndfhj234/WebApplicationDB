<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create User - Admin Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null || (!userRole.equals("admin") && !userRole.equals("super_admin"))) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    
    <nav class="navbar">
        <div class="navbar-content">
            <h1>Admin Dashboard</h1>
            <div class="nav-links">
                <a href="admin.jsp">Dashboard</a>
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

        <div class="form-container">
            <h3>Create New User</h3>
            <form action="AdminServlet" method="post">
                <input type="hidden" name="action" value="create">
                
                <div class="form-group">
                    <label for="userName">Username</label>
                    <input type="text" id="userName" name="userName" class="form-control" required>
                </div>
                
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" required>
                </div>
                
                <div class="form-group">
                    <label for="userRole">Role</label>
                    <select id="userRole" name="userRole" class="form-control" required>
                        <option value="user">User</option>
                        <% if (userRole.equals("super_admin")) { %>
                            <option value="admin">Admin</option>
                        <% } %>
                    </select>
                </div>
                
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Create User</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
