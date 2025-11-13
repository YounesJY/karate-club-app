<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test Details - Karate Club</title>
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
    <c:if test="${empty test}">
        <div class="alert alert-warning">Test not found.</div>
        <a href="tests" class="btn btn-outline-secondary"><i class="fas fa-arrow-left"></i> Back</a>
    </c:if>
    <c:if test="${not empty test}">
        <div class="row">
            <div class="col-md-7">
                <div class="card mb-4">
                    <div class="card-header"><h5 class="mb-0"><i class="fas fa-info-circle"></i> Test Details</h5></div>
                    <div class="card-body">
                        <table class="table table-sm">
                            <tbody>
                            <tr><th>ID</th><td>${test.testID}</td></tr>
                            <tr><th>Member</th><td>${test.member.person.name} <span class="text-muted">#${test.member.memberID}</span></td></tr>
                            <tr><th>Rank</th><td><span class="badge bg-info">${test.rank.rankName}</span></td></tr>
                            <tr><th>Instructor</th><td>${test.testedByInstructor.person.name}</td></tr>
                            <tr><th>Date</th><td>${test.date}</td></tr>
                            <tr><th>Status</th><td>
                                <c:choose>
                                    <c:when test="${test.result eq 'Passed'}"><span class="badge bg-success">Passed</span></c:when>
                                    <c:when test="${test.result eq 'Failed'}"><span class="badge bg-danger">Failed</span></c:when>
                                    <c:otherwise><span class="badge bg-secondary">${test.result}</span></c:otherwise>
                                </c:choose>
                            </td></tr>
                            <tr><th>Payment</th><td>
                                <c:choose>
                                    <c:when test="${not empty test.payment}">Paid <span class="text-muted">#${test.payment.paymentID}</span></c:when>
                                    <c:otherwise><span class="badge bg-warning text-dark">Unpaid</span></c:otherwise>
                                </c:choose>
                            </td></tr>
                            </tbody>
                        </table>
                        <div class="d-flex gap-2">
                            <a href="tests" class="btn btn-outline-secondary btn-sm"><i class="fas fa-arrow-left"></i> Back</a>
                            <form action="tests" method="post" onsubmit="return confirm('Delete this test?');">
                                <input type="hidden" name="action" value="delete"/>
                                <input type="hidden" name="id" value="${test.testID}"/>
                                <button type="submit" class="btn btn-outline-danger btn-sm"><i class="fas fa-trash"></i> Delete</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-5">
                <div class="card mb-4">
                    <div class="card-header"><h5 class="mb-0"><i class="fas fa-edit"></i> Record Result</h5></div>
                    <div class="card-body">
                        <c:if test="${not empty sessionScope.errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${sessionScope.errorMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                            <c:remove var="errorMessage" scope="session"/>
                        </c:if>
                        <form method="post" action="tests">
                            <input type="hidden" name="action" value="recordResult"/>
                            <input type="hidden" name="testId" value="${test.testID}"/>
                            <div class="mb-3">
                                <label class="form-label">Result</label>
                                <select name="result" class="form-select" required>
                                    <option value="true">Pass</option>
                                    <option value="false">Fail</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Payment ID</label>
                                <input type="number" name="paymentId" class="form-control" />
                            </div>
                            <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Save Result</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
