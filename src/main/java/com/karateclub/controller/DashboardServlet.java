// ENHANCED DashboardServlet.java
package com.karateclub.controller;

import com.karateclub.model.UserRole;
import com.karateclub.service.DashboardService;
import com.karateclub.util.SecurityUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {
    private DashboardService dashboardService;

    @Override
    public void init() {
        dashboardService = new DashboardService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication using SecurityUtil
        if (SecurityUtil.redirectIfNotLoggedIn(request, response)) {
            return;
        }

        String userRole = SecurityUtil.getCurrentUserRole(request);
        String redirectPage;

        try {
            switch (UserRole.valueOf(userRole)) {
                case ADMIN:
                    setupAdminDashboard(request);
                    redirectPage = "/WEB-INF/views/dashboards/admin-dashboard.jsp";
                    break;
                case INSTRUCTOR:
                    setupInstructorDashboard(request);
                    redirectPage = "/WEB-INF/views/dashboards/instructor-dashboard.jsp";
                    break;
                case MEMBER:
                    setupMemberDashboard(request);
                    redirectPage = "/WEB-INF/views/dashboards/member-profile.jsp";
                    break;
                default:
                    redirectPage = "/WEB-INF/views/auth/access-denied.jsp";
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(redirectPage);
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private void setupAdminDashboard(HttpServletRequest request) {
        java.util.Map<String, Object> stats = dashboardService.getDashboardStats();
        java.util.List<java.util.Map<String, String>> activities = dashboardService.getRecentActivities(5);
        request.setAttribute("dashboardStats", stats);
        request.setAttribute("recentActivities", activities);
    }

    private void setupInstructorDashboard(HttpServletRequest request) {
        Integer instructorId = SecurityUtil.getCurrentUserId(request);
        if (instructorId != null) {
            java.util.Map<String, Object> instructorData = dashboardService.getInstructorDashboard(instructorId);
            request.setAttribute("instructorData", instructorData);
        }
    }

    private void setupMemberDashboard(HttpServletRequest request) {
        Integer memberId = SecurityUtil.getCurrentUserId(request);
        if (memberId != null) {
            java.util.Map<String, Object> memberProfile = dashboardService.getMemberProfile(memberId);
            request.setAttribute("memberProfile", memberProfile);
        }
    }
}