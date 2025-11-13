<!-- [file name]: report.jsp (Payments - Reports) -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payments Report - Karate Club</title>
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
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>
            <i class="fas fa-chart-line"></i> Payments Report
        </h2>
        <a href="payments" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left"></i> Back
        </a>
    </div>

    <div class="card mb-3">
        <div class="card-body">
            <form method="get" action="payments">
                <input type="hidden" name="action" value="report" />
                <div class="row g-3 align-items-end">
                    <div class="col-md-4">
                        <label for="startDate" class="form-label">Start Date *</label>
                        <input type="date" id="startDate" name="startDate" class="form-control"
                               value="${startDate}" required />
                    </div>
                    <div class="col-md-4">
                        <label for="endDate" class="form-label">End Date *</label>
                        <input type="date" id="endDate" name="endDate" class="form-control"
                               value="${endDate}" required />
                    </div>
                    <div class="col-md-4 text-end">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-search"></i> Run Report
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <c:if test="${not empty payments}">
        <div class="row mb-3">
            <div class="col-md-4">
                <div class="card bg-success text-white">
                    <div class="card-body text-center">
                        <div>Total Revenue</div>
                        <h3>$<fmt:formatNumber value="${totalRevenue}" pattern=",#0.00"/></h3>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">Payments from ${startDateFormatted} to ${endDateFormatted}</h5>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Member</th>
                            <th>Date</th>
                            <th class="text-end">Amount</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="p" items="${payments}">
                            <tr>
                                <td>${p.paymentID}</td>
                                <td>${p.member.person.name}</td>
                                <td>${p.formattedDate}</td>
                                <td class="text-end">$<fmt:formatNumber value="${p.amount}" pattern=",#0.00"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${empty payments && not empty startDate && not empty endDate}">
        <div class="alert alert-warning">No payments found for the selected date range.</div>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
