<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Access Denied - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6 text-center">
                <div class="card shadow">
                    <div class="card-body p-5">
                        <div class="text-danger mb-4">
                            <i class="fas fa-ban fa-5x"></i>
                        </div>
                        <h1 class="text-danger">Access Denied</h1>
                        <p class="lead">You don't have permission to access this resource.</p>
                        <p class="text-muted">Role: ${userRole}</p>
                        <div class="mt-4">
                            <a href="dashboard" class="btn btn-primary">Return to Dashboard</a>
                            <a href="logout" class="btn btn-outline-secondary">Logout</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>