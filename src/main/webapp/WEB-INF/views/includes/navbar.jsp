<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
            <i class="fas fa-fist-raised"></i> Karate Club
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link ${param.currentPage eq 'dashboard' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/dashboard">
                        <i class="fas fa-tachometer-alt me-1"></i>Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${param.currentPage eq 'members' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/members">
                        <i class="fas fa-users me-1"></i>Members
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${param.currentPage eq 'instructors' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/instructors">
                        <i class="fas fa-chalkboard-teacher me-1"></i>Instructors
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${param.currentPage eq 'tests' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/tests">
                        <i class="fas fa-certificate me-1"></i>Tests
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${param.currentPage eq 'payments' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/payments">
                        <i class="fas fa-money-bill-wave me-1"></i>Payments
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <span class="navbar-text text-light me-3">
                        <i class="fas fa-user me-1"></i>${username} (${userRole})
                    </span>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-warning" href="${pageContext.request.contextPath}/logout">
                        <i class="fas fa-sign-out-alt me-1"></i>Logout
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>