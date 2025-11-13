package com.karateclub.controller;

import com.karateclub.dao.BeltTestDAO;
import com.karateclub.model.BeltTest;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/instructor/tests")
public class InstructorTestsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer instructorId = (Integer) request.getSession().getAttribute("personId");

        if (instructorId == null) {
            response.sendRedirect("../login");
            return;
        }

        try {
            // Only get tests for this instructor
            BeltTestDAO testDAO = new BeltTestDAO();
            List<BeltTest> myTests = testDAO.findByInstructor(instructorId);

            request.setAttribute("tests", myTests);
            request.getRequestDispatcher("/WEB-INF/views/instructors/my-tests.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("../error");
        }
    }
}