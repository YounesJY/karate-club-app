<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Profile - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="../includes/instructor-navbar.jsp"/>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2><i class="fas fa-user me-2"></i>My Profile</h2>
                    <span class="badge bg-info">INSTRUCTOR</span>
                </div>

                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">Personal Information</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Name:</strong> ${instructor.person.name}</p>
                                <p><strong>Qualification:</strong> ${instructor.qualification}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Contact Info:</strong> ${instructor.person.contactInfo}</p>
                                <p><strong>Address:</strong> ${instructor.person.address}</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Read-only notice -->
                <div class="alert alert-info mt-4">
                    <i class="fas fa-info-circle me-2"></i>
                    <strong>Profile updates:</strong> Please contact the administrator to update your profile information.
                </div>

                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-1"></i>Back to Dashboard
                    </a>
                    <button class="btn btn-outline-primary" disabled>
                        <i class="fas fa-edit me-1"></i>Edit Profile (Contact Admin)
                    </button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>