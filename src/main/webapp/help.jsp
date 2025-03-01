<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Help - Social Media Platform</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%
        String userName = (String) session.getAttribute("user");
        if (userName == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    
    <nav class="navbar">
        <div class="navbar-content">
            <h1>Connect</h1>
            <div class="nav-links">
                <a href="landing.jsp">Home</a>
                <a href="profile.jsp">Profile</a>
                <a href="users.jsp">Users</a>
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
            <h3>Contact Admin</h3>
            <form action="HelpServlet" method="post">
                <div class="form-group">
                    <label for="message">Message</label>
                    <textarea id="message" name="message" class="form-control" rows="4" required></textarea>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Send Message</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
