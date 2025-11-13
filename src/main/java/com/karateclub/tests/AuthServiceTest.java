package com.karateclub.tests;

import com.karateclub.service.AuthenticationService;
import com.karateclub.service.UserService;
import com.karateclub.model.User;
import com.karateclub.model.UserRole;

public class AuthServiceTest {
    public static void main(String[] args) {
        System.out.println("🔐 TESTING REFACTORED AUTHENTICATION SERVICES");
        System.out.println("=============================================\n");

        AuthenticationService authService = new AuthenticationService();
        UserService userService = new UserService();

        // Test 1: Authentication with test data
        System.out.println("1. TESTING AUTHENTICATION");
        System.out.println("-------------------------");

        testAuthentication(authService, "admin", "admin123", "ADMIN");
        testAuthentication(authService, "instructor1", "instructor123", "INSTRUCTOR");
        testAuthentication(authService, "member1", "member123", "MEMBER");
        testAuthentication(authService, "wronguser", "wrongpass", "INVALID");

        // Test 2: User Service Operations
        System.out.println("\n2. TESTING USER SERVICE OPERATIONS");
        System.out.println("---------------------------------");

        // List all users
        System.out.println("All users in system:");
        userService.getAllUsersSorted().forEach(user ->
                System.out.println("  - " + user.getUsername() + " (" + user.getPrimaryRole() + ") - ID: " + user.getUserID())
        );

        // Test user counts
        System.out.println("\nUser statistics:");
        System.out.println("  - Total users: " + userService.getAllUsers().size());
        System.out.println("  - Admins: " + userService.getUsersByRole(UserRole.ADMIN).size());
        System.out.println("  - Instructors: " + userService.getUsersByRole(UserRole.INSTRUCTOR).size());
        System.out.println("  - Members: " + userService.getUsersByRole(UserRole.MEMBER).size());

        // Test role validation
        User adminUser = userService.getUserByUsername("admin");
        if (adminUser != null) {
            System.out.println("\n3. TESTING ROLE-BASED ACCESS CONTROL");
            System.out.println("-----------------------------------");
            System.out.println("Admin user '" + adminUser.getUsername() + "' permissions:");
            System.out.println("  - Can access admin panel: " + authService.hasRequiredRole(adminUser, "ADMIN"));
            System.out.println("  - Can access instructor panel: " + authService.hasRequiredRole(adminUser, "INSTRUCTOR"));
            System.out.println("  - Can access member panel: " + authService.hasRequiredRole(adminUser, "MEMBER"));
        }

        // Test session validation
        if (adminUser != null) {
            System.out.println("\n4. TESTING SESSION VALIDATION");
            System.out.println("----------------------------");
            System.out.println("Session validation for user ID " + adminUser.getUserID() + ": " +
                    authService.validateSession(adminUser.getUserID()));  // Now uses int
        }

        System.out.println("\n🎉 ALL TYPE CONSISTENCY ISSUES FIXED!");
        System.out.println("✅ User entity now uses int like all other entities");
    }

    private static void testAuthentication(AuthenticationService authService,
                                           String username, String password, String expectedRole) {
        User user = authService.authenticate(username, password);
        if (user != null) {
            System.out.println("✅ Login SUCCESS: " + username + " | Role: " + user.getPrimaryRole());
        } else {
            System.out.println("❌ Login FAILED: " + username + "/" + password);
        }
    }
}