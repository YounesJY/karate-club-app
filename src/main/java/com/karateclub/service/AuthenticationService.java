// src/main/java/com/karateclub/service/AuthenticationService.java
package com.karateclub.service;

import com.karateclub.dao.UserDAO;
import com.karateclub.model.User;

public class AuthenticationService {
    private UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Authenticate user with username and password
     */
    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return null;
        }

        User user = userDAO.findByUsername(username.trim());

        if (user != null && user.getPassword().equals(password.trim())) {
            return user; // Authentication successful
        }

        return null; // Authentication failed
    }

    /**
     * Check if user has required role
     */
    public boolean hasRequiredRole(User user, String requiredRole) {
        if (user == null || requiredRole == null) return false;

        switch (requiredRole.toUpperCase()) {
            case "ADMIN":
                return user.isAdmin();
            case "INSTRUCTOR":
                return user.isInstructor();
            case "MEMBER":
                return user.isMember();
            default:
                return false;
        }
    }

    /**
     * Validate session - check if user is still valid in database
     */
    public boolean validateSession(int userId) {  // Changed to int
        return userDAO.getById(userId) != null;
    }
}