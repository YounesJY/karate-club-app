<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Promote Member - Karate Club</title>
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
                        <i class="fas fa-arrow-up"></i> Promote Member
                    </h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                    </c:if>

                    <div class="mb-4 p-3 bg-light rounded">
                        <h5>Member Information</h5>
                        <div class="row">
                            <div class="col-md-6">
                                <strong>Name:</strong> ${member.person.name}
                            </div>
                            <div class="col-md-6">
                                <strong>Current Belt:</strong>
                                <span class="badge bg-info">${member.lastBeltRank.rankName}</span>
                            </div>
                            <div class="col-md-6 mt-2">
                                <strong>Member ID:</strong> ${member.memberID}
                            </div>
                            <div class="col-md-6 mt-2">
                                <strong>Status:</strong>
                                <span class="badge ${member.active ? 'bg-success' : 'bg-danger'}">
                                    ${member.active ? 'Active' : 'Inactive'}
                                </span>
                            </div>
                        </div>
                    </div>

                    <form action="members" method="post">
                        <input type="hidden" name="action" value="promote">
                        <input type="hidden" name="id" value="${member.memberID}">

                        <div class="mb-3">
                            <label for="newRankId" class="form-label">Select New Belt Rank *</label>
                            <select class="form-select" id="newRankId" name="newRankId" required>
                                <option value="">Select Belt Rank</option>
                                <c:forEach var="rank" items="${availableRanks}">
                                    <option value="${rank.rankID}">${rank.rankName}</option>
                                </c:forEach>
                            </select>
                            <div class="form-text">
                                Current rank: ${member.lastBeltRank.rankName}
                            </div>
                        </div>

                        <div class="mb-3">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="confirmEligibility" required>
                                <label class="form-check-label" for="confirmEligibility">
                                    I confirm this member is eligible for promotion
                                </label>
                            </div>
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="members" class="btn btn-secondary me-md-2">Cancel</a>
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-arrow-up"></i> Promote Member
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.querySelector('form').addEventListener('submit', function(e) {
        const newRank = document.getElementById('newRankId').value;

        if (!newRank) {
            e.preventDefault();
            alert('Please select a new belt rank!');
            return false;
        }

        if (!document.getElementById('confirmEligibility').checked) {
            e.preventDefault();
            alert('Please confirm eligibility for promotion');
            return false;
        }
    });
</script>
</body>
</html>