<!-- [file name]: index.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Karate Club Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">
            <i class="fas fa-fist-raised"></i> Karate Club
        </a>
    </div>
</nav>

<div class="container mt-5">
    <div class="row">
        <div class="col-md-3 mb-4">
            <div class="card text-white bg-primary">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h4>Members</h4>
                            <h2>${memberCount}</h2>
                        </div>
                        <i class="fas fa-users fa-3x"></i>
                    </div>
                    <a href="members" class="btn btn-light btn-sm mt-2">Manage</a>
                </div>
            </div>
        </div>

        <div class="col-md-3 mb-4">
            <div class="card text-white bg-success">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h4>Instructors</h4>
                            <h2>${instructorCount}</h2>
                        </div>
                        <i class="fas fa-chalkboard-teacher fa-3x"></i>
                    </div>
                    <a href="instructors" class="btn btn-light btn-sm mt-2">Manage</a>
                </div>
            </div>
        </div>

        <div class="col-md-3 mb-4">
            <div class="card text-white bg-warning">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h4>Payments</h4>
                            <h2>${paymentCount}</h2>
                        </div>
                        <i class="fas fa-money-bill-wave fa-3x"></i>
                    </div>
                    <a href="payments" class="btn btn-light btn-sm mt-2">Manage</a>
                </div>
            </div>
        </div>

        <div class="col-md-3 mb-4">
            <div class="card text-white bg-info">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h4>Tests</h4>
                            <h2>${testCount}</h2>
                        </div>
                        <i class="fas fa-trophy fa-3x"></i>
                    </div>
                    <a href="tests" class="btn btn-light btn-sm mt-2">Manage</a>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h5>Quick Actions</h5>
                </div>
                <div class="card-body">
                    <div class="d-grid gap-2">
                        <a href="members?action=new" class="btn btn-outline-primary">
                            <i class="fas fa-plus"></i> Add New Member
                        </a>
                        <a href="instructors?action=new" class="btn btn-outline-success">
                            <i class="fas fa-plus"></i> Add New Instructor
                        </a>
                        <a href="payments?action=new" class="btn btn-outline-warning">
                            <i class="fas fa-plus"></i> Record Payment
                        </a>
                        <a href="tests?action=new" class="btn btn-outline-info">
                            <i class="fas fa-plus"></i> Schedule Test
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h5>Recent Activity</h5>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">
                            <small class="text-muted">Today</small><br>
                            <span>5 new payments received</span>
                        </li>
                        <li class="list-group-item">
                            <small class="text-muted">Yesterday</small><br>
                            <span>3 members promoted</span>
                        </li>
                        <li class="list-group-item">
                            <small class="text-muted">This week</small><br>
                            <span>12 belt tests scheduled</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>