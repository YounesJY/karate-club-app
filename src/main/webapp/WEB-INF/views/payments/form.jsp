<!-- [file name]: form.jsp (Payments - New/Process) -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>New Payment - Karate Club</title>
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
            <a class="nav-link" href="instructors">Instructors</a>
            <a class="nav-link active" href="payments">Payments</a>
            <a class="nav-link" href="tests">Tests</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>
                    <i class="fas fa-plus-circle"></i> New Payment
                </h2>
                <a href="payments" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left"></i> Back
                </a>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
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
                    <!-- Use process endpoint so Member lookup is handled server-side -->
                    <form method="post" action="payments?action=process">
                        <div class="mb-3">
                            <label for="memberId" class="form-label">Member ID *</label>
                            <input type="number" class="form-control" id="memberId" name="memberId"
                                   value="${param.memberId}" required min="1" placeholder="Enter Member ID" />
                            <div class="form-text">Enter the ID of the paying member.</div>
                        </div>

                        <div class="mb-3">
                            <label for="amount" class="form-label">Amount (USD) *</label>
                            <input type="number" step="0.01" min="0.01" class="form-control" id="amount" name="amount"
                                   placeholder="0.00" required />
                        </div>

                        <div class="alert alert-info">
                            <i class="fas fa-info-circle"></i>
                            The payment date will be set to today. If you need historical entry, add it later.
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="payments" class="btn btn-secondary">Cancel</a>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-check"></i> Process Payment
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

