package com.karateclub.controller;

import com.karateclub.dao.InstructorDAO;
import com.karateclub.model.Instructor;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/instructor/profile")
public class InstructorProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get current instructor ID from session
        Integer personId = (Integer) request.getSession().getAttribute("personId");

        if (personId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Only allow access to own profile
            InstructorDAO instructorDAO = new InstructorDAO();
            Instructor instructor = instructorDAO.findByPersonId(personId);

            if (instructor == null) {
                // ✅ FIXED: Redirect to the correct access-denied URL
                response.sendRedirect(request.getContextPath() + "/access-denied");
                return;
            }

            request.setAttribute("instructor", instructor);
            request.getRequestDispatcher("/WEB-INF/views/instructors/profile-view.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/access-denied");
        }
    }
}