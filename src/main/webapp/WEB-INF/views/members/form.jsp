<!-- [file name]: form.jsp (Members) -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${empty member ? 'Add' : 'Edit'} Member - Karate Club</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">
            <i class="fas fa-fist-raised"></i> Karate Club
        </a>
    </div>
</nav>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h4 class="mb-0">
                        <i class="fas ${empty member ? 'fa-plus' : 'fa-edit'}"></i>
                        ${empty member ? 'Add New Member' : 'Edit Member'}
                    </h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                    </c:if>

                    <form action="members" method="post">
                        <input type="hidden" name="action" value="${empty member ? 'create' : 'update'}">
                        <c:if test="${not empty member}">
                            <input type="hidden" name="id" value="${member.memberID}">
                        </c:if>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="name" class="form-label">Full Name *</label>
                                <input type="text" class="form-control" id="name" name="name"
                                       value="${not empty member.person ? member.person.name : ''}" required>
                            </div>
                            <div class="col-md-6">
                                <label for="contactInfo" class="form-label">Contact Info *</label>
                                <input type="text" class="form-control" id="contactInfo" name="contactInfo"
                                       value="${not empty member.person ? member.person.contactInfo : ''}" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="emergencyContact" class="form-label">Emergency Contact *</label>
                            <input type="text" class="form-control" id="emergencyContact"
                                   name="emergencyContact" value="${not empty member ? member.emergencyContactInfo : ''}" required>
                            <div class="form-text">At least 5 characters long</div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="beltRank" class="form-label">Belt Rank</label>
                                <select class="form-select" id="beltRank" name="beltRankId">
                                    <option value="">Select Belt Rank</option>
                                    <!-- Belt ranks would be populated from database -->
                                    <option value="1" ${not empty member.lastBeltRank && member.lastBeltRank.rankID == 1 ? 'selected' : ''}>
                                        White Belt
                                    </option>
                                    <option value="2" ${not empty member.lastBeltRank && member.lastBeltRank.rankID == 2 ? 'selected' : ''}>
                                        Yellow Belt
                                    </option>
                                    <option value="3" ${not empty member.lastBeltRank && member.lastBeltRank.rankID == 3 ? 'selected' : ''}>
                                        Orange Belt
                                    </option>
                                    <!-- Add more belt ranks as needed -->
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Status</label>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="active" name="active"
                                    ${not empty member && member.active ? 'checked' : ''}>
                                    <label class="form-check-label" for="active">
                                        Active Member
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="members" class="btn btn-secondary me-md-2">Cancel</a>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i>
                                ${empty member ? 'Create Member' : 'Update Member'}
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