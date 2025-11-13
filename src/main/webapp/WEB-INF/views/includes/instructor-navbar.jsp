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