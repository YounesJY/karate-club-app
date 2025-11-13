// src/main/java/com/karateclub/controller/DashboardServlet.java
package com.karateclub.controller;

import com.karateclub.model.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null) {
            response.sendRedirect("login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        String redirectPage;

        switch (UserRole.valueOf(userRole)) {
            case ADMIN:
                redirectPage = "/WEB-INF/views/dashboards/admin-dashboard.jsp";
                break;
            case INSTRUCTOR:
                redirectPage = "/WEB-INF/views/dashboards/instructor-dashboard.jsp";
                break;
            case MEMBER:
                redirectPage = "/WEB-INF/views/dashboards/member-profile.jsp";
                break;
            default:
                redirectPage = "/WEB-INF/views/auth/access-denied.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(redirectPage);
        dispatcher.forward(request, response);
    }
}