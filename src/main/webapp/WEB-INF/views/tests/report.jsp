<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Belt Tests Report - Karate Club</title>
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
    <div class="card mb-3">
        <div class="card-body">
            <form class="row g-3" method="get" action="tests">
                <input type="hidden" name="action" value="report"/>
                <div class="col-md-4">
                    <label class="form-label">Start Date</label>
                    <input type="date" name="startDate" value="${startDate}" class="form-control"/>
                </div>
                <div class="col-md-4">
                    <label class="form-label">End Date</label>
                    <input type="date" name="endDate" value="${endDate}" class="form-control"/>
                </div>
                <div class="col-md-4 d-flex align-items-end gap-2">
                    <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i> Filter</button>
                    <a href="tests" class="btn btn-outline-secondary"><i class="fas fa-arrow-left"></i> Back</a>
                </div>
            </form>
        </div>
    </div>

    <div class="card">
        <div class="card-header"><i class="fas fa-list"></i> Results</div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Member</th>
                        <th>Rank</th>
                        <th>Instructor</th>
                        <th>Date</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="t" items="${tests}">
                        <tr>
                            <td>${t.testID}</td>
                            <td>${t.member.person.name}</td>
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
                        </tr>
                    </c:forEach>
                    <c:if test="${empty tests}">
                        <tr><td colspan="6" class="text-center text-muted">No tests found for the selected range.</td></tr>
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
