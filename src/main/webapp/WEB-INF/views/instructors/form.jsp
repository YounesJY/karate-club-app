<!-- [file name]: form.jsp (Instructor Form) -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>
        <c:choose>
            <c:when test="${not empty instructor}">Edit Instructor - Karate Club</c:when>
            <c:otherwise>Add Instructor - Karate Club</c:otherwise>
        </c:choose>
    </title>
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
            <a class="nav-link active" href="instructors">Instructors</a>
            <a class="nav-link" href="payments">Payments</a>
            <a class="nav-link" href="tests">Tests</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>
                    <i class="fas fa-chalkboard-teacher"></i>
                    <c:choose>
                        <c:when test="${not empty instructor}">Edit Instructor</c:when>
                        <c:otherwise>Add Instructor</c:otherwise>
                    </c:choose>
                </h2>
                <a href="instructors" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left"></i> Back
                </a>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="card">
                <div class="card-body">
                    <form method="post"
                          action="instructors?action=${not empty instructor ? 'update' : 'create'}">

                        <c:if test="${not empty instructor}">
                            <input type="hidden" name="id" value="${instructor.instructorID}" />
                            <input type="hidden" name="personId" value="${instructor.person.personID}" />
                        </c:if>

                        <div class="mb-3">
                            <label for="name" class="form-label">Name *</label>
                            <input type="text" class="form-control" id="name" name="name" required maxlength="100" value="${instructor.person.name}" placeholder="Instructor full name" />
                        </div>
                        <div class="mb-3">
                            <label for="address" class="form-label">Address</label>
                            <input type="text" class="form-control" id="address" name="address" maxlength="100" value="${instructor.person.address}" placeholder="Street, City" />
                        </div>
                        <div class="mb-3">
                            <label for="contactInfo" class="form-label">Contact Info *</label>
                            <input type="text" class="form-control" id="contactInfo" name="contactInfo" required maxlength="100" value="${instructor.person.contactInfo}" placeholder="Phone or Email" />
                        </div>
                        <div class="mb-3">
                            <label for="qualification" class="form-label">Qualification *</label>
                            <input type="text" class="form-control" id="qualification" name="qualification" required maxlength="100" value="${instructor.qualification}" placeholder="e.g., 3rd Dan Black Belt" />
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="instructors" class="btn btn-secondary">Cancel</a>
                            <button type="submit" class="btn btn-primary">
                                <c:choose>
                                    <c:when test="${not empty instructor}">
                                        <i class="fas fa-save"></i> Update
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fas fa-plus"></i> Create
                                    </c:otherwise>
                                </c:choose>
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