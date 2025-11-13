<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tests by Member - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="index.jsp"><i class="fas fa-fist-raised"></i> Karate Club</a>
        <div class="navbar-nav">
            <a class="nav-link" href="members">Members</a>
            <a class="nav-link" href="instructors">Instructors</a>
            <a class="nav-link" href="payments">Payments</a>
            <a class="nav-link active" href="tests">Tests</a>
        </div>
    </div>
</nav>
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0"><i class="fas fa-user"></i> Tests for Member #${memberId}</h4>
        <a href="tests" class="btn btn-outline-secondary btn-sm"><i class="fas fa-arrow-left"></i> Back</a>
    </div>
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Rank</th>
                        <th>Instructor</th>
                        <th>Date</th>
                        <th>Status</th>
                        <th>Payment</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="t" items="${tests}">
                        <tr>
                            <td>${t.testID}</td>
                            <td><span class="badge bg-info">${t.rank.rankName}</span></td>
                            <td>${t.testedByInstructor.person.name}</td>
                            <td>${t.date}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${t.result eq 'Passed'}"><span class="badge bg-success">Passed</span></c:when>
                                    <c:when test="${t.result eq 'Failed'}"><span class="badge bg-danger">Failed</span></c:when>
                                    <c:otherwise><span class="badge bg-secondary">${t.result}</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:choose><c:when test="${not empty t.payment}">#${t.payment.paymentID}</c:when><c:otherwise>—</c:otherwise></c:choose></td>
                            <td><a href="tests?action=view&id=${t.testID}" class="btn btn-sm btn-outline-primary"><i class="fas fa-eye"></i></a></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty tests}">
                        <tr><td colspan="7" class="text-center text-muted">No tests found.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
