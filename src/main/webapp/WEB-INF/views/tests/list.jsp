<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tests - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="dashboard">
            <i class="fas fa-fist-raised"></i> Karate Club
        </a>
        <div class="navbar-nav">
            <a class="nav-link" href="members">Members</a>
            <a class="nav-link" href="instructors">Instructors</a>
            <a class="nav-link" href="payments">Payments</a>
            <a class="nav-link active" href="tests">Tests</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0"><i class="fas fa-certificate"></i> Belt Tests</h2>
        <div>
            <a href="tests?action=new" class="btn btn-primary"><i class="fas fa-plus"></i> Schedule Test</a>
            <a href="tests?action=report" class="btn btn-outline-secondary"><i class="fas fa-chart-line"></i> Report</a>
        </div>
    </div>

    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${sessionScope.errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <div class="card">
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
                        <th>Result</th>
                        <th>Payment</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="t" items="${tests}">
                        <tr>
                            <td>${t.testID}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty t.member}">
                                        ${t.member.person.name} <span class="text-muted">#${t.member.memberID}</span>
                                    </c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </td>
                            <td><span class="badge bg-info">${t.rank.rankName}</span></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty t.testedByInstructor}">
                                        ${t.testedByInstructor.person.name}
                                    </c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${t.date}</td>
                            <td>
                                <!-- FIXED: Removed duplicated c:choose blocks -->
                                <c:choose>
                                    <c:when test="${t.result}">
                                        <span class="badge bg-success">PASS</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">FAIL</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty t.payment}">
                                        Paid <span class="text-muted">#${t.payment.paymentID}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark">Unpaid</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <a class="btn btn-outline-primary" href="tests?action=view&id=${t.testID}">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <form method="post" action="tests" onsubmit="return confirm('Delete this test?');" style="display: inline;">
                                        <input type="hidden" name="action" value="delete"/>
                                        <input type="hidden" name="id" value="${t.testID}"/>
                                        <button type="submit" class="btn btn-outline-danger">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty tests}">
                        <tr>
                            <td colspan="8" class="text-center text-muted">No tests found.</td>
                        </tr>
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