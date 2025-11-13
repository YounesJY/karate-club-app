// src/main/java/com/karateclub/controller/ProfileServlet.java
package com.karateclub.controller;

import com.karateclub.util.SecurityUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Redirect based on role to appropriate profile page
        String userRole = SecurityUtil.getCurrentUserRole(request);
        String redirectPage;

        switch (userRole) {
            case "ADMIN":
                redirectPage = "/WEB-INF/views/dashboards/admin-dashboard.jsp";
                break;
            case "INSTRUCTOR":
                redirectPage = "/WEB-INF/views/dashboards/instructor-dashboard.jsp";
                break;
            case "MEMBER":
                redirectPage = "/WEB-INF/views/dashboards/member-profile.jsp";
                break;
            default:
                redirectPage = "/WEB-INF/views/auth/login.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(redirectPage);
        dispatcher.forward(request, response);
    }
}