<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error - Social Media Platform</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .error-container {
            text-align: center;
            padding: 4rem 2rem;
        }
        
        .error-code {
            font-size: 6rem;
            font-weight: bold;
            color: #1877f2;
            margin-bottom: 1rem;
        }
        
        .error-message {
            font-size: 1.5rem;
            color: #1c1e21;
            margin-bottom: 2rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="error-container">
            <div class="error-code">
                <%= response.getStatus() %>
            </div>
            <div class="error-message">
                <% if (exception != null) { %>
                    <%= exception.getMessage() %>
                <% } else { %>
                    Oops! Something went wrong.
                <% } %>
            </div>
            <a href="landing.jsp" class="btn btn-primary">Back to Home</a>
        </div>
    </div>
</body>
</html>
