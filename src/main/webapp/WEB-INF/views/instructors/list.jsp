<!-- [file name]: list.jsp (Instructors) -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Instructors - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">
            <i class="fas fa-fist-raised"></i> Karate Club
        </a>
        <div class="navbar-nav">
            <a class="nav-link" href="members">Members</a>
            <a class="nav-link active" href="instructors">Instructors</a>
            <a class="nav-link" href="payments">Payments</a>
            <a class="nav-link" href="tests">Tests</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>
            <i class="fas fa-chalkboard-teacher"></i> Instructors Management
        </h2>
        <a href="instructors?action=new" class="btn btn-primary">
            <i class="fas fa-plus"></i> Add New Instructor
        </a>
    </div>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>

    <div class="row">
        <c:forEach var="instructor" items="${instructors}">
            <div class="col-md-4 mb-4">
                <div class="card">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <i class="fas fa-user-tie fa-3x text-primary"></i>
                        </div>
                        <h5 class="card-title">${instructor.person.name}</h5>
                        <p class="card-text">
                            <span class="badge bg-info">${instructor.qualification}</span>
                        </p>
                        <p class="card-text text-muted">
                            <small>Students: ${instructor.memberInstructors.size()}</small>
                        </p>
                        <p class="card-text small mb-1"><i class="fas fa-envelope"></i> ${instructor.person.contactInfo}</p>
                        <p class="card-text small text-muted">${instructor.person.address}</p>
                        <div class="btn-group btn-group-sm">
                            <a href="instructors?action=viewStudents&id=${instructor.instructorID}"
                               class="btn btn-outline-primary">
                                <i class="fas fa-users"></i>
                            </a>
                            <a href="instructors?action=edit&id=${instructor.instructorID}"
                               class="btn btn-outline-secondary">
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="instructors?action=delete&id=${instructor.instructorID}"
                               class="btn btn-outline-danger"
                               onclick="return confirm('Delete this instructor?')">
                                <i class="fas fa-trash"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty instructors}">
            <div class="col-12">
                <div class="text-center text-muted py-5">
                    <i class="fas fa-chalkboard-teacher fa-3x mb-3"></i>
                    <h4>No instructors found</h4>
                    <p>Get started by adding your first instructor.</p>
                    <a href="instructors?action=new" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Add Instructor
                    </a>
                </div>
            </div>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>