<!-- Assign Student to Instructor (assign-student.jsp) -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Assign Student to ${instructor.person.name} - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="index.jsp"><i class="fas fa-fist-raised"></i> Karate Club</a>
        <div class="navbar-nav">
            <a class="nav-link" href="members">Members</a>
            <a class="nav-link active" href="instructors">Instructors</a>
            <a class="nav-link" href="payments">Payments</a>
            <a class="nav-link" href="tests">Tests</a>
        </div>
    </div>
</nav>
<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="fas fa-user-plus"></i> Assign Student to Instructor</h2>
                <a href="instructors?action=viewStudents&id=${instructor.instructorID}" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left"></i> Back to Students
                </a>
            </div>
            <div class="card mb-3">
                <div class="card-header">
                    <h5 class="card-title mb-0">Instructor: ${instructor.person.name}</h5>
                    <p class="text-muted mb-0">Qualification: ${instructor.qualification}</p>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">${errorMessage}<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
                    </c:if>
                    <form method="post" action="instructors?action=assignStudent">
                        <input type="hidden" name="instructorId" value="${instructor.instructorID}" />
                        <div class="mb-3">
                            <label for="memberId" class="form-label">Member ID *</label>
                            <input type="number" class="form-control" id="memberId" name="memberId" required min="1" placeholder="Enter Member ID" />
                            <div class="form-text">Enter the ID of the member to assign to this instructor.</div>
                        </div>
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="instructors?action=viewStudents&id=${instructor.instructorID}" class="btn btn-secondary">Cancel</a>
                            <button type="submit" class="btn btn-primary"><i class="fas fa-user-plus"></i> Assign Student</button>
                        </div>
                    </form>
                </div>
            </div>
            <div class="card">
                <div class="card-header"><h6 class="card-title mb-0"><i class="fas fa-lightbulb"></i> Demo Instructions</h6></div>
                <div class="card-body">
                    <p>For testing, you'll need:</p>
                    <ul>
                        <li>A valid Member ID from your database</li>
                        <li>The member shouldn't already be assigned to this instructor</li>
                        <li>Both member and instructor must exist</li>
                    </ul>
                    <p class="mb-0 text-muted"><small>In production, this would be a searchable dropdown of available members.</small></p>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>