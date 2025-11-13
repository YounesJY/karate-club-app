// src/main/java/com/karateclub/util/SecurityUtil.java
package com.karateclub.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class SecurityUtil {

    // Check if user is logged in
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }

    // Check if user has ADMIN role
    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && "ADMIN".equals(session.getAttribute("userRole"));
    }

    // Check if user has INSTRUCTOR role (or higher)
    public static boolean isInstructor(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        String role = (String) session.getAttribute("userRole");
        return "INSTRUCTOR".equals(role) || "ADMIN".equals(role);
    }
    // Check if user has specific role
    public static boolean hasRole(HttpServletRequest request, String requiredRole) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        String userRole = (String) session.getAttribute("userRole");
        if ("ADMIN".equals(userRole)) return true; // Admin has all roles

        return requiredRole.equals(userRole);
    }

    // Get current user ID
    public static Integer getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (Integer) session.getAttribute("userId") : null;
    }

    // Get current user role
    public static String getCurrentUserRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (String) session.getAttribute("userRole") : null;
    }

    // Redirect if not logged in
    public static boolean redirectIfNotLoggedIn(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isLoggedIn(request)) {
            response.sendRedirect("login");
            return true; // redirected
        }
        return false; // not redirected
    }

    // Redirect if not admin
    public static boolean redirectIfNotAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("access-denied");
            return true; // redirected
        }
        return false; // not redirected
    }
}