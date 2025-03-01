<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Operation Results - Admin Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .result-container {
            background-color: var(--card-bg);
            border-radius: 12px;
            padding: 1.5rem;
            box-shadow: var(--shadow);
            margin: 2rem 0;
            max-height: 600px;
            overflow-y: auto;
        }
        
        .result-item {
            padding: 12px;
            border-bottom: 1px solid var(--border-color);
            display: flex;
            justify-content: space-between;
        }
        
        .result-item:last-child {
            border-bottom: none;
        }
        
        .success {
            color: green;
        }
        
        .error {
            color: red;
        }
        
        .summary {
            text-align: center;
            font-size: 1.2rem;
            margin: 1rem 0;
            padding: 1rem;
            background-color: var(--light-bg);
            border-radius: 8px;
            font-weight: bold;
        }
        
        .redirect-note {
            text-align: center;
            margin-top: 2rem;
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
        
        // Get action type from request
        String actionType = request.getParameter("action");
    %>
    
    <nav class="navbar">
        <div class="navbar-content">
            <h1>Admin Dashboard</h1>
            <div class="nav-links">
                <a href="admin.jsp">Dashboard</a>
                <a href="create.jsp">Create User</a>
                <a href="update.jsp">Update User</a>
                <a href="delete.jsp">Delete User</a>
                <a href="LogoutServlet">Logout</a>
            </div>
        </div>
    </nav>
    
    <div class="container">
        <h2>
            <c:choose>
                <c:when test="${param.action eq 'delete'}">
                    User Deletion Results
                </c:when>
                <c:otherwise>
                    Update Results
                </c:otherwise>
            </c:choose>
        </h2>
        
        <div class="summary ${sessionScope.bulkUpdateSuccess ? 'success' : 'error'}">
            <c:choose>
                <c:when test="${empty sessionScope.bulkUpdateResults}">
                    No changes were requested.
                </c:when>
                <c:when test="${sessionScope.bulkUpdateSuccessCount == 0}">
                    <c:choose>
                        <c:when test="${param.action eq 'delete'}">
                            No users were deleted. Please check the errors below.
                        </c:when>
                        <c:otherwise>
                            No users were updated. Please check the errors below.
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:when test="${sessionScope.bulkUpdateSuccess}">
                    <c:choose>
                        <c:when test="${param.action eq 'delete'}">
                            Successfully deleted ${sessionScope.bulkUpdateSuccessCount} users!
                        </c:when>
                        <c:otherwise>
                            Successfully updated ${sessionScope.bulkUpdateSuccessCount} users!
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${param.action eq 'delete'}">
                            Deleted ${sessionScope.bulkUpdateSuccessCount} users with some errors.
                        </c:when>
                        <c:otherwise>
                            Updated ${sessionScope.bulkUpdateSuccessCount} users with some errors.
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </div>
        
        <c:if test="${not empty sessionScope.bulkUpdateResults}">
            <div class="result-container">
                <c:forEach var="result" items="${sessionScope.bulkUpdateResults}">
                    <div class="result-item">
                        <span><strong>${result.key}</strong></span>
                        <span class="${result.value.startsWith('Error') ? 'error' : 'success'}">
                            ${result.value}
                        </span>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        
        <div class="redirect-note">
            <a href="admin.jsp" class="btn btn-primary">Return to Dashboard</a>
        </div>
    </div>
    
    <div class="footer">
        <p>Admin Dashboard - Operation Results</p>
    </div>
    
    <%
        // Clean up session attributes after displaying
        session.removeAttribute("bulkUpdateResults");
        session.removeAttribute("bulkUpdateSuccess");
        session.removeAttribute("bulkUpdateSuccessCount");
    %>
</body>
</html>