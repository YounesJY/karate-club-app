<!-- [file name]: students.jsp (Instructor's Students) -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${instructor.person.name}'s Students - Karate Club</title>
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
        <div>
            <h2>
                <i class="fas fa-users"></i> ${instructor.person.name}'s Students
            </h2>
            <p class="text-muted mb-0">
                Qualification: <span class="badge bg-info">${instructor.qualification}</span>
            </p>
        </div>
        <div>
            <a href="instructors?action=assignStudent&id=${instructor.instructorID}"
               class="btn btn-primary me-2">
                <i class="fas fa-user-plus"></i> Assign Student
            </a>
            <a href="instructors" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left"></i> Back to Instructors
            </a>
        </div>
    </div>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <div class="card">
        <div class="card-header">
            <h5 class="card-title mb-0">
                <i class="fas fa-list"></i> Assigned Students (${memberAssignments.size()})
            </h5>
        </div>
        <div class="card-body">
            <c:if test="${not empty memberAssignments}">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Belt Rank</th>
                            <th>Assigned Date</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="assignment" items="${memberAssignments}">
                            <tr>
                                <td>${assignment.member.memberID}</td>
                                <td>${assignment.member.person.name}</td>
                                <td>
                                    <span class="badge bg-warning text-dark">
                                        ${assignment.member.lastBeltRank.rankName}
                                    </span>
                                </td>
                                <td>${assignment.assignDate}</td>
                                <td>
                                    <form method="post" action="instructors?action=removeStudent"
                                          style="display: inline;">
                                        <input type="hidden" name="instructorId" value="${instructor.instructorID}">
                                        <input type="hidden" name="memberId" value="${assignment.member.memberID}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                                onclick="return confirm('Remove this student from instructor?')">
                                            <i class="fas fa-user-minus"></i> Remove
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <c:if test="${empty memberAssignments}">
                <div class="text-center text-muted py-5">
                    <i class="fas fa-users-slash fa-3x mb-3"></i>
                    <h4>No students assigned</h4>
                    <p>This instructor doesn't have any assigned students yet.</p>
                    <a href="instructors?action=assignStudent&id=${instructor.instructorID}"
                       class="btn btn-primary">
                        <i class="fas fa-user-plus"></i> Assign First Student
                    </a>
                </div>
            </c:if>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
