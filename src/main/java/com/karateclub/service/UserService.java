// src/main/java/com/karateclub/service/UserService.java
package com.karateclub.service;

import com.karateclub.dao.UserDAO;
import com.karateclub.model.User;
import com.karateclub.model.UserRole;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Create new user with validation
    public boolean createUser(String username, String password, UserRole role, int personId) {
        if (userDAO.usernameExists(username)) {
            return false; // Username already exists
        }

        // Note: In production, we'd hash the password here
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Plain text for demo
        user.setPrimaryRole(role);

        userDAO.save(user);
        return true;
    }

    // Get user by ID
    public User getUserById(int userId) {  // int
        return userDAO.getById(userId);
    }

    // Get user by username
    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    // Get all users sorted
    public List<User> getAllUsersSorted() {
        return userDAO.findAllSorted();
    }

    // Get users by role
    public List<User> getUsersByRole(UserRole role) {
        return userDAO.findByRole(role);
    }

    // Update user
    public boolean updateUser(User user) {
        try {
            userDAO.update(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Delete user
    public boolean deleteUser(int userId) {  // int
        try {
            userDAO.delete(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Change user role
    public boolean changeUserRole(int userId, UserRole newRole) {  // int
        User user = userDAO.getById(userId);
        if (user != null) {
            user.setPrimaryRole(newRole);
            userDAO.update(user);
            return true;
        }
        return false;
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        return userDAO.usernameExists(username);
    }
}