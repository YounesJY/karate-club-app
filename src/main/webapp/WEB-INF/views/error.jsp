<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6 text-center">
                <div class="card shadow">
                    <div class="card-body p-5">
                        <div class="text-danger mb-4">
                            <i class="fas fa-exclamation-triangle fa-5x"></i>
                        </div>
                        <h1 class="text-danger">Oops! Something went wrong</h1>
                        <p class="lead">We encountered an error processing your request.</p>
                        <div class="mt-4">
                            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">
                                Return to Dashboard
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>