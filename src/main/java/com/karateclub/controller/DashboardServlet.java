package com.karateclub.controller;

import com.karateclub.model.UserRole;
import com.karateclub.service.DashboardService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;


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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null) {
            response.sendRedirect("login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        String redirectPage;

        try {
            switch (UserRole.valueOf(userRole)) {
                case ADMIN:
                    // Load admin dashboard data
                    java.util.Map<String, Object> stats = dashboardService.getDashboardStats();
                    java.util.List<Map<String, String>> activities = dashboardService.getRecentActivities(5);
                    request.setAttribute("dashboardStats", stats);
                    request.setAttribute("recentActivities", activities);
                    redirectPage = "/WEB-INF/views/dashboards/admin-dashboard.jsp";
                    break;

                case INSTRUCTOR:
                    // Load instructor-specific data
                    Integer instructorId = (Integer) session.getAttribute("personId");
                    if (instructorId != null) {
                        Map<String, Object> instructorData = dashboardService.getInstructorDashboard(instructorId);
                        request.setAttribute("instructorData", instructorData);
                    }
                    redirectPage = "/WEB-INF/views/dashboards/instructor-dashboard.jsp";
                    break;

                case MEMBER:
                    // Load member profile data
                    Integer memberId = (Integer) session.getAttribute("personId");
                    if (memberId != null) {
                        Map<String, Object> memberProfile = dashboardService.getMemberProfile(memberId);
                        request.setAttribute("memberProfile", memberProfile);
                    }
                    redirectPage = "/WEB-INF/views/dashboards/member-profile.jsp";
                    break;

                default:
                    redirectPage = "/WEB-INF/views/auth/access-denied.jsp";
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(redirectPage);
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error");
        }
    }
}