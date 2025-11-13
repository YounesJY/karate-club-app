<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session != null && session.getAttribute("userId") != null) {
        // User is logged in - redirect to dashboard
        response.sendRedirect("dashboard");
    } else {
        // User is not logged in - redirect to login
        response.sendRedirect("login");
    }
%>