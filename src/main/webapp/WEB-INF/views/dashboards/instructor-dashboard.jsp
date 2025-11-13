<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Instructor Dashboard - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .stat-card {
            border-radius: 10px;
            transition: transform 0.2s;
            border-left: 4px solid !important;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
        .welcome-header {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            color: white;
            padding: 2rem;
            border-radius: 10px;
            margin-bottom: 2rem;
        }
        .student-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: #3498db;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <!-- Top Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                <i class="fas fa-fist-raised"></i> Karate Club
            </a>
            <div class="navbar-nav">
                <a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">
                    <i class="fas fa-tachometer-alt me-1"></i>Dashboard
                </a>
                <a class="nav-link" href="${pageContext.request.contextPath}/instructor/tests">
                    <i class="fas fa-certificate me-1"></i>My Tests
                </a>
                <a class="nav-link" href="${pageContext.request.contextPath}/instructor/profile">
                    <i class="fas fa-user-edit me-1"></i>My Profile
                </a>
                <a class="nav-link text-warning" href="${pageContext.request.contextPath}/logout">
                    <i class="fas fa-sign-out-alt me-1"></i>Logout (${username})
                </a>
            </div>
        </div>
    </nav>

    <!-- Main content -->
    <div class="container-fluid mt-4">
        <div class="row">
            <!-- Welcome Header -->
            <div class="col-12">
                <div class="welcome-header">
                    <div class="row align-items-center">
                        <div class="col">
                            <h1 class="h3 mb-1">Welcome, Sensei ${username}!</h1>
                            <p class="mb-0">Role: ${userRole} | Qualification: ${instructorData.instructor.qualification}</p>
                        </div>
                        <div class="col-auto">
                            <span class="badge bg-light text-dark fs-6">INSTRUCTOR PANEL</span>
                        </div>
                    </div>
                </div>
            </div>

                <!-- Quick Stats -->
                <div class="row mb-4">
                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card stat-card border-left-primary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                            My Students</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            ${instructorData.students.size()}
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-users fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card stat-card border-left-success shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                            Upcoming Tests</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            ${instructorData.upcomingTests.size()}
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-graduation-cap fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card stat-card border-left-info shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                            Tests This Month</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">4</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-calendar fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-3 col-md-6 mb-4">
                        <div class="card stat-card border-left-warning shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                            Pass Rate</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">85%</div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-chart-line fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- My Students & Upcoming Tests -->
                <div class="row">
                    <!-- My Students -->
                    <div class="col-lg-6 mb-4">
                        <div class="card shadow">
                            <div class="card-header py-3 d-flex justify-content-between align-items-center">
                                <h6 class="m-0 font-weight-bold text-primary">My Students</h6>
                                <span class="badge bg-primary">${instructorData.students.size()} students</span>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Student</th>
                                                <th>Current Belt</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="student" items="${instructorData.students}">
                                                <tr>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="student-avatar me-3">
                                                                ${student.person.name.charAt(0)}
                                                            </div>
                                                            <div>
                                                                <strong>${student.person.name}</strong>
                                                                <div class="text-muted small">${student.person.contactInfo}</div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-info">${student.lastBeltRank.rankName}</span>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${student.active}">
                                                                <span class="badge bg-success">Active</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">Inactive</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty instructorData.students}">
                                                <tr>
                                                    <td colspan="3" class="text-center text-muted py-4">
                                                        <i class="fas fa-users fa-2x mb-2"></i><br>
                                                        No students assigned yet
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Upcoming Tests -->
                    <div class="col-lg-6 mb-4">
                        <div class="card shadow">
                            <div class="card-header py-3 d-flex justify-content-between align-items-center">
                                <h6 class="m-0 font-weight-bold text-primary">Upcoming Tests</h6>
                                <a href="tests?action=new" class="btn btn-sm btn-primary">
                                    <i class="fas fa-plus me-1"></i>Schedule Test
                                </a>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Student</th>
                                                <th>Belt</th>
                                                <th>Date</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="test" items="${instructorData.upcomingTests}">
                                                <tr>
                                                    <td>
                                                        <strong>${test.member.person.name}</strong>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-warning text-dark">${test.rank.rankName}</span>
                                                    </td>
                                                    <td>${test.date}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${test.result}">
                                                                <span class="badge bg-success">Passed</span>
                                                            </c:when>
                                                            <c:when test="${!test.result}">
                                                                <span class="badge bg-danger">Failed</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">Scheduled</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty instructorData.upcomingTests}">
                                                <tr>
                                                    <td colspan="4" class="text-center text-muted py-4">
                                                        <i class="fas fa-graduation-cap fa-2x mb-2"></i><br>
                                                        No upcoming tests
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Quick Actions -->
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card shadow">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Quick Actions</h6>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-3 mb-3">
                                        <a href="tests?action=new" class="btn btn-primary btn-block w-100">
                                            <i class="fas fa-graduation-cap me-2"></i>Schedule Test
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                       <a href="instructor/tests" class="btn btn-success btn-block w-100">
                                            <i class="fas fa-list me-2"></i>View All Tests
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <a href="instructor/profile"
                                           class="btn btn-info btn-block w-100">
                                            <i class="fas fa-user-edit me-2"></i>Edit Profile
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <button class="btn btn-warning btn-block w-100" disabled
                                                title="Not accessible to instructors">
                                            <i class="fas fa-ban me-2"></i>Manage Payments
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>