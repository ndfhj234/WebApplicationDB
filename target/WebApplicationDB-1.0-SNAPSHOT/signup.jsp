<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Sign Up - Connect</title>
    <link rel="stylesheet" href="css/style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-content">
            <h1><a href="index.html" style="text-decoration: none; color: inherit;">Connect</a></h1>
            <div class="nav-links">
                <a href="login.jsp">Login</a>
                <a href="signup.jsp">Sign Up</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="form-container">
            <h2 class="form-title">Join Connect Today</h2>
            
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="message message-error">
                    <svg viewBox="0 0 24 24" width="24" height="24" style="vertical-align: middle; margin-right: 10px;">
                        <path fill="#d32f2f" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/>
                    </svg>
                    ${sessionScope.errorMessage}
                    <% session.removeAttribute("errorMessage"); %>
                </div>
            </c:if>
            
            <form action="SignupServlet" method="post">
                <div class="form-group">
                    <label for="userName">Username</label>
                    <input type="text" id="userName" name="userName" class="form-control" required>
                </div>
                
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" required>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                </div>
                
                <div class="form-group">
                    <button type="submit" class="btn btn-secondary" style="width: 100%;">Create Account</button>
                </div>
            </form>
            
            <div style="text-align: center; margin-top: 1.5rem;">
                <p>Already have an account? <a href="login.jsp" style="font-weight: 600;">Login</a></p>
            </div>
        </div>  
    </div>
    
    <footer class="footer">
        <p> Oriondo | Repollo | Sarvida </p>
    </footer>
</body>
</html>
