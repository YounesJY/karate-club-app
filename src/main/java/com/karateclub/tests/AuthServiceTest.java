// src/main/java/com/karateclub/tests/AuthServiceTest.java
package com.karateclub.tests;

import com.karateclub.service.AuthenticationService;
import com.karateclub.service.UserService;
import com.karateclub.model.User;

import java.util.List;

public class AuthServiceTest {
    public static void main(String[] args) {
        System.out.println("🔐 TESTING AUTHENTICATION SERVICE");
        System.out.println("=================================\n");

        AuthenticationService authService = new AuthenticationService();
        UserService userService = new UserService();

        // Test 1: Authentication with database users
        System.out.println("1. TESTING AUTHENTICATION WITH DATABASE USERS");
        System.out.println("---------------------------------------------");

        testAuthentication(authService, "admin", "admin123");
        testAuthentication(authService, "instructor1", "instructor123");
        testAuthentication(authService, "member1", "member123");
        testAuthentication(authService, "wronguser", "wrongpass");

        // Test 2: User Service Operations
        System.out.println("\n2. TESTING USER SERVICE OPERATIONS");
        System.out.println("----------------------------------");

        try {
            // List all users
            List<User> users = userService.getAllUsers();
            System.out.println("📊 Total users in database: " + users.size());

            // Show user details
            System.out.println("👥 Users in system:");
            users.forEach(user ->
                    System.out.println("  - " + user.getUsername() + " (" + user.getPrimaryRole() + ")")
            );

            // Test role-based access
            User adminUser = userService.getUserByUsername("admin");
            if (adminUser != null) {
                System.out.println("\n3. TESTING ROLE-BASED ACCESS CONTROL");
                System.out.println("-----------------------------------");
                System.out.println("Admin user permissions:");
                System.out.println("  - Is ADMIN: " + adminUser.isAdmin());
                System.out.println("  - Is INSTRUCTOR: " + adminUser.isInstructor());
                System.out.println("  - Is MEMBER: " + adminUser.isMember());
            }

        } catch (Exception e) {
            System.out.println("❌ Error during user service test: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n🎉 AUTHENTICATION SERVICE TEST COMPLETED!");
    }

    private static void testAuthentication(AuthenticationService authService,
                                           String username, String password) {
        try {
            User user = authService.authenticate(username, password);
            if (user != null) {
                System.out.println("✅ Login SUCCESS: " + username +
                        " | Role: " + user.getPrimaryRole() +
                        " | UserID: " + user.getUserID());
            } else {
                System.out.println("❌ Login FAILED: " + username + "/" + password);
            }
        } catch (Exception e) {
            System.out.println("❌ Error testing " + username + ": " + e.getMessage());
        }
    }
}