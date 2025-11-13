<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Tests - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="../includes/instructor-navbar.jsp"/>

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-certificate me-2"></i>My Scheduled Tests</h2>
            <span class="badge bg-primary">${tests.size()} tests</span>
        </div>

        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>

        <div class="card shadow">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>Student</th>
                                <th>Belt Rank</th>
                                <th>Test Date</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="test" items="${tests}">
                                <tr>
                                    <td>
                                        <strong>${test.member.person.name}</strong>
                                        <div class="text-muted small">ID: ${test.member.memberID}</div>
                                    </td>
                                    <td>
                                        <span class="badge bg-info">${test.rank.rankName}</span>
                                    </td>
                                    <td>${test.date}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${test.result}">
                                                <span class="badge bg-success">PASSED</span>
                                            </c:when>
                                            <c:when test="${!test.result}">
                                                <span class="badge bg-danger">FAILED</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning">SCHEDULED</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="tests?action=view&id=${test.testID}" class="btn btn-sm btn-outline-primary">
                                            <i class="fas fa-eye"></i> View
                                        </a>
                                        <!-- No edit/delete buttons for instructors -->
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty tests}">
                                <tr>
                                    <td colspan="5" class="text-center text-muted py-4">
                                        <i class="fas fa-graduation-cap fa-2x mb-2"></i><br>
                                        No tests scheduled for you.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">
                <i class="fas fa-arrow-left me-1"></i>Back to Dashboard
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>