<%-- src/main/webapp/WEB-INF/views/dashboards/admin-dashboard.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            background: #2c3e50;
            color: white;
            min-height: 100vh;
            padding: 0;
        }
        .sidebar .nav-link {
            color: #bdc3c7;
            padding: 1rem 1.5rem;
            border-left: 4px solid transparent;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            color: white;
            background: #34495e;
            border-left: 4px solid #3498db;
        }
        .stat-card {
            border-radius: 10px;
            transition: transform 0.2s;
            border-left: 4px solid !important;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
        .welcome-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem;
            border-radius: 10px;
            margin-bottom: 2rem;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block sidebar">
                <div class="position-sticky pt-3">
                    <div class="text-center p-3">
                        <h4>🥋 Karate Club</h4>
                        <small>Administration Panel</small>
                    </div>

                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">
                                <i class="fas fa-tachometer-alt me-2"></i>Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/members">
                                <i class="fas fa-users me-2"></i>Members
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/instructors">
                                <i class="fas fa-chalkboard-teacher me-2"></i>Instructors
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/tests">
                                <i class="fas fa-graduation-cap me-2"></i>Belt Tests
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/payments">
                                <i class="fas fa-money-bill-wave me-2"></i>Payments
                            </a>
                        </li>
                        <li class="nav-item mt-4">
                            <a class="nav-link text-warning" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt me-2"></i>Logout
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 py-4">
                <!-- Welcome Header -->
                <div class="welcome-header">
                    <div class="row align-items-center">
                        <div class="col">
                            <h1 class="h3 mb-1">Welcome, ${username}!</h1>
                            <p class="mb-0">Role: ${userRole} | Last login: Today</p>
                        </div>
                        <div class="col-auto">
                            <span class="badge bg-light text-dark fs-6">ADMIN PANEL</span>
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
                                            Total Members</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${dashboardStats.totalMembers}" default="24"/>
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
                                            Active Members</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${dashboardStats.activeMembers}" default="18"/>
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-user-check fa-2x text-gray-300"></i>
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
                                            Monthly Revenue</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            $<c:out value="${dashboardStats.monthlyRevenue}" default="1250.0"/>
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-dollar-sign fa-2x text-gray-300"></i>
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
                                            Pending Tests</div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <c:out value="${dashboardStats.pendingTests}" default="2"/>
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="fas fa-graduation-cap fa-2x text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Recent Activity & Quick Actions -->
                <div class="row">
                    <!-- Recent Activity -->
                    <div class="col-lg-8 mb-4">
                        <div class="card shadow">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Recent Activity</h6>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered" width="100%" cellspacing="0">
                                        <thead>
                                            <tr>
                                                <th>Member</th>
                                                <th>Activity</th>
                                                <th>Time</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="activity" items="${recentActivities}">
                                                <tr>
                                                    <td>${activity.member}</td>
                                                    <td>${activity.action}</td>
                                                    <td>${activity.time}</td>
                                                    <td>
                                                        <span class="badge bg-${activity.status == 'success' ? 'success' : 'warning'}">
                                                            ${activity.status}
                                                        </span>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty recentActivities}">
                                                <tr>
                                                    <td colspan="4" class="text-center text-muted">
                                                        No recent activities
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Quick Actions -->
                    <div class="col-lg-4 mb-4">
                        <div class="card shadow">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Quick Actions</h6>
                            </div>
                            <div class="card-body">
                                <div class="d-grid gap-2">
                                    <a href="${pageContext.request.contextPath}/members?action=new"
                                       class="btn btn-primary btn-block">
                                        <i class="fas fa-user-plus me-2"></i>Add New Member
                                    </a>
                                    <a href="${pageContext.request.contextPath}/tests?action=new"
                                       class="btn btn-success btn-block">
                                        <i class="fas fa-graduation-cap me-2"></i>Schedule Test
                                    </a>
                                    <a href="${pageContext.request.contextPath}/payments?action=new"
                                       class="btn btn-info btn-block">
                                        <i class="fas fa-money-bill-wave me-2"></i>Process Payment
                                    </a>
                                    <a href="${pageContext.request.contextPath}/members"
                                       class="btn btn-warning btn-block">
                                        <i class="fas fa-list me-2"></i>View All Members
                                    </a>
                                </div>
                            </div>
                        </div>

                        <!-- System Status -->
                        <div class="card shadow mt-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">System Status</h6>
                            </div>
                            <div class="card-body">
                                <div class="mb-3">
                                    <small class="text-success"><i class="fas fa-circle me-1"></i> Database: Connected</small>
                                </div>
                                <div class="mb-3">
                                    <small class="text-success"><i class="fas fa-circle me-1"></i> Authentication: Active</small>
                                </div>
                                <div class="mb-3">
                                    <small class="text-success"><i class="fas fa-circle me-1"></i> Services: Running</small>
                                </div>
                                <div class="mt-3">
                                    <small class="text-muted">Last updated: Just now</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Auto-dismiss alerts after 5 seconds
        setTimeout(function() {
            var alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                var bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    </script>
</body>
</html>