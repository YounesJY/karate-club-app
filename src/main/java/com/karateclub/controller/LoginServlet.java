// src/main/java/com/karateclub/controller/LoginServlet.java
package com.karateclub.controller;

import com.karateclub.model.User;
import com.karateclub.model.UserRole;
import com.karateclub.service.AuthenticationService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private AuthenticationService authService;

    @Override
    public void init() {
        authService = new AuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If already logged in, redirect to dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect("dashboard");
            return;
        }

        // Show login page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = authService.authenticate(username, password);

        if (user != null) {
            // Create session and store user info
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getUserID());
            session.setAttribute("userRole", user.getPrimaryRole().toString());
            session.setAttribute("personId", user.getPerson().getPersonID());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("user", user);

            // Redirect based on role
            response.sendRedirect("dashboard");
        } else {
            // Authentication failed
            request.setAttribute("errorMessage", "Invalid username or password");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp");
            dispatcher.forward(request, response);
        }
    }
}