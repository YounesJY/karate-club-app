<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .profile-header {
            background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
            color: white;
            padding: 2rem;
            border-radius: 10px;
            margin-bottom: 2rem;
        }
        .belt-progress {
            height: 20px;
            border-radius: 10px;
            overflow: hidden;
        }
        .test-badge {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            color: white;
        }
        .stat-card {
            border-radius: 10px;
            transition: transform 0.2s;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
    </style>
</head>
<body>
    <!-- Member Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                <i class="fas fa-fist-raised"></i> Karate Club
            </a>
            <div class="navbar-nav">
                <a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">
                    <i class="fas fa-user me-1"></i>My Profile
                </a>
                <a class="nav-link text-warning" href="${pageContext.request.contextPath}/logout">
                    <i class="fas fa-sign-out-alt me-1"></i>Logout
                </a>
            </div>
        </div>
    </nav>

    <div class="container-fluid mt-4">
        <!-- Profile Header -->
        <div class="profile-header">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="h3 mb-1">Welcome, ${memberProfile.member.person.name}!</h1>
                    <p class="mb-0">
                        Current Rank: <strong>${memberProfile.member.lastBeltRank.rankName}</strong> |
                        Member Since: 2024 |
                        Status: <span class="badge ${memberProfile.member.active ? 'bg-success' : 'bg-secondary'}">
                            ${memberProfile.member.active ? 'Active' : 'Inactive'}
                        </span>
                    </p>
                </div>
                <div class="col-md-4 text-end">
                    <span class="badge bg-light text-dark fs-6">MEMBER PROFILE</span>
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
                                    Tests Taken</div>
                                <div class="h5 mb-0 font-weight-bold text-gray-800">
                                    ${memberProfile.testHistory.size()}
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
                <div class="card stat-card border-left-success shadow h-100 py-2">
                    <div class="card-body">
                        <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                                <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                    Pass Rate</div>
                                <div class="h5 mb-0 font-weight-bold text-gray-800">
                                    <c:set var="passedTests" value="0" />
                                    <c:forEach var="test" items="${memberProfile.testHistory}">
                                        <c:if test="${test.result}">
                                            <c:set var="passedTests" value="${passedTests + 1}" />
                                        </c:if>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${memberProfile.testHistory.size() > 0}">
                                            ${(passedTests / memberProfile.testHistory.size() * 100)}%
                                        </c:when>
                                        <c:otherwise>0%</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="col-auto">
                                <i class="fas fa-chart-line fa-2x text-gray-300"></i>
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
                                    Current Belt</div>
                                <div class="h5 mb-0 font-weight-bold text-gray-800">
                                    ${memberProfile.member.lastBeltRank.rankName}
                                </div>
                            </div>
                            <div class="col-auto">
                                <i class="fas fa-award fa-2x text-gray-300"></i>
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
                                    Subscription</div>
                                <div class="h5 mb-0 font-weight-bold text-gray-800">
                                    <c:choose>
                                        <c:when test="${memberProfile.member.active}">
                                            <span class="text-success">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-danger">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="col-auto">
                                <i class="fas fa-calendar-check fa-2x text-gray-300"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Personal Info & Test History -->
        <div class="row">
            <!-- Personal Information -->
            <div class="col-lg-4 mb-4">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="fas fa-user me-2"></i>Personal Information</h5>
                    </div>
                    <div class="card-body">
                        <p><strong>Full Name:</strong> ${memberProfile.member.person.name}</p>
                        <p><strong>Contact Info:</strong> ${memberProfile.member.person.contactInfo}</p>
                        <p><strong>Emergency Contact:</strong> ${memberProfile.member.emergencyContactInfo}</p>
                        <p><strong>Current Belt:</strong>
                            <span class="badge bg-info">${memberProfile.member.lastBeltRank.rankName}</span>
                        </p>
                        <p><strong>Member Status:</strong>
                            <span class="badge ${memberProfile.member.active ? 'bg-success' : 'bg-secondary'}">
                                ${memberProfile.member.active ? 'Active' : 'Inactive'}
                            </span>
                        </p>
                    </div>
                </div>

                <!-- Belt Progress -->
                <div class="card shadow mt-4">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0"><i class="fas fa-trophy me-2"></i>Belt Journey</h5>
                    </div>
                    <div class="card-body">
                        <div class="belt-progress bg-light mb-3">
                            <div class="bg-success" style="width: ${(memberProfile.member.lastBeltRank.rankID / 7) * 100}%; height: 100%;"></div>
                        </div>
                        <small class="text-muted">
                            Progress: ${memberProfile.member.lastBeltRank.rankName}
                            (${memberProfile.member.lastBeltRank.rankID} of 7 belts)
                        </small>
                    </div>
                </div>
            </div>

            <!-- Test History -->
            <div class="col-lg-8 mb-4">
                <div class="card shadow">
                    <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="fas fa-history me-2"></i>My Test History</h5>
                        <span class="badge bg-light text-dark">${memberProfile.testHistory.size()} tests</span>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Belt Rank</th>
                                        <th>Instructor</th>
                                        <th>Result</th>
                                        <th>Notes</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="test" items="${memberProfile.testHistory}">
                                        <tr>
                                            <td>${test.date}</td>
                                            <td>
                                                <span class="badge bg-info">${test.rank.rankName}</span>
                                            </td>
                                            <td>${test.testedByInstructor.person.name}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${test.result}">
                                                        <span class="badge bg-success">PASSED</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger">FAILED</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${test.result}">
                                                        <small class="text-success">Promoted to ${test.rank.rankName}</small>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <small class="text-muted">Needs improvement</small>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty memberProfile.testHistory}">
                                        <tr>
                                            <td colspan="5" class="text-center text-muted py-4">
                                                <i class="fas fa-graduation-cap fa-2x mb-2"></i><br>
                                                No test history yet. Your journey begins now!
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Payment Status -->
                <div class="card shadow mt-4">
                    <div class="card-header bg-warning text-dark">
                        <h5 class="mb-0"><i class="fas fa-money-bill-wave me-2"></i>Payment Status</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty memberProfile.paymentHistory}">
                                <p><strong>Last Payment:</strong>
                                    $${memberProfile.paymentHistory[0].amount} on ${memberProfile.paymentHistory[0].date}
                                </p>
                                <p><strong>Total Payments:</strong> ${memberProfile.paymentHistory.size()}</p>
                                <p><strong>Status:</strong>
                                    <span class="badge bg-success">Up to date</span>
                                </p>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">No payment history available.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>