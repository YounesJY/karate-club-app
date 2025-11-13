<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Members - Karate Club</title>
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
            <a class="nav-link" href="payments">Payments</a>
            <a class="nav-link" href="tests">Tests</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>
            <i class="fas fa-users"></i> Members Management
        </h2>
        <a href="members?action=new" class="btn btn-primary">
            <i class="fas fa-plus"></i> Add New Member
        </a>
    </div>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <div class="card">
        <div class="card-header">
            <div class="row">
                <div class="col-md-6">
                    <ul class="nav nav-pills">
                        <li class="nav-item">
                            <a class="nav-link ${empty param.filter ? 'active' : ''}" href="members">All</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link ${param.filter == 'active' ? 'active' : ''}"
                               href="members?filter=active">Active</a>
                        </li>
                    </ul>
                </div>
                <div class="col-md-6">
                    <form class="d-flex" method="get" action="members">
                        <input class="form-control me-2" type="search" placeholder="Search members..."
                               name="search" value="${param.search}">
                        <button class="btn btn-outline-success" type="submit">Search</button>
                    </form>
                </div>
            </div>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Contact</th>
                        <th>Belt Rank</th>
                        <th>Status</th>
                        <th>Emergency Contact</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="member" items="${members}">
                        <tr>
                            <td>${member.memberID}</td>
                            <td>${member.person.name}</td>
                            <td>${member.person.contactInfo}</td>
                            <td>
                                <span class="badge bg-info">${member.lastBeltRank.rankName}</span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${member.active}">
                                        <span class="badge bg-success">Active</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">Inactive</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${member.emergencyContactInfo}</td>
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <a href="members?action=edit&id=${member.memberID}"
                                       class="btn btn-outline-primary">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <c:choose>
                                        <c:when test="${member.active}">
                                            <a href="members?action=deactivate&id=${member.memberID}"
                                               class="btn btn-outline-warning"
                                               onclick="return confirm('Deactivate this member?')">
                                                <i class="fas fa-pause"></i>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="members?action=activate&id=${member.memberID}"
                                               class="btn btn-outline-success"
                                               onclick="return confirm('Activate this member?')">
                                                <i class="fas fa-play"></i>
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                    <a href="members?action=promote&id=${member.memberID}"
                                       class="btn btn-outline-info">
                                        <i class="fas fa-arrow-up"></i>
                                    </a>

                                    <%-- Replace the delete link with a form --%>
                                    <form action="members" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${member.memberID}">
                                        <button type="submit" class="btn btn-outline-danger btn-sm"
                                                onclick="return confirm('Delete this member permanently?')">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty members}">
                        <tr>
                            <td colspan="7" class="text-center text-muted">
                                No members found.
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>