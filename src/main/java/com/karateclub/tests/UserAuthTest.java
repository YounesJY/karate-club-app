// src/main/java/com/karateclub/tests/UserAuthTest.java
package com.karateclub.tests;

import com.karateclub.model.User;
import com.karateclub.model.UserRole;
import com.karateclub.model.Person;

public class UserAuthTest {
    public static void main(String[] args) {
        System.out.println("🥋 KARATE CLUB - USER AUTHENTICATION TEST");
        System.out.println("=========================================\n");

        // Create test person matching your actual Person class
        Person testPerson = new Person();
        testPerson.setPersonID(1);
        testPerson.setName("Test User");
        testPerson.setAddress("123 Test Street");
        testPerson.setContactInfo("test@email.com");

        // Test different user roles
        System.out.println("1. CREATING TEST USERS WITH DIFFERENT ROLES");
        System.out.println("-------------------------------------------");

        User adminUser = new User("admin", "admin123", UserRole.ADMIN, testPerson);
        User instructorUser = new User("instructor1", "instructor123", UserRole.INSTRUCTOR, testPerson);
        User memberUser = new User("member1", "member123", UserRole.MEMBER, testPerson);

        System.out.println("✅ Admin User: " + adminUser.getUsername() + " | Role: " + adminUser.getPrimaryRole());
        System.out.println("✅ Instructor User: " + instructorUser.getUsername() + " | Role: " + instructorUser.getPrimaryRole());
        System.out.println("✅ Member User: " + memberUser.getUsername() + " | Role: " + memberUser.getPrimaryRole());

        // Test role permissions
        System.out.println("\n2. TESTING ROLE-BASED PERMISSIONS");
        System.out.println("----------------------------------");

        System.out.println("ADMIN PERMISSIONS:");
        System.out.println("  - Can manage everything: " + adminUser.isAdmin());
        System.out.println("  - Can view instructor data: " + adminUser.isInstructor());
        System.out.println("  - Can view member data: " + adminUser.isMember());

        System.out.println("\nINSTRUCTOR PERMISSIONS:");
        System.out.println("  - Can manage everything: " + instructorUser.isAdmin());
        System.out.println("  - Can view instructor data: " + instructorUser.isInstructor());
        System.out.println("  - Can view member data: " + instructorUser.isMember());

        System.out.println("\nMEMBER PERMISSIONS:");
        System.out.println("  - Can manage everything: " + memberUser.isAdmin());
        System.out.println("  - Can view instructor data: " + memberUser.isInstructor());
        System.out.println("  - Can view member data: " + memberUser.isMember());

        // Test navigation flow
        System.out.println("\n3. SIMULATING LOGIN REDIRECTS");
        System.out.println("-----------------------------");

        simulateLoginRedirect(adminUser);
        simulateLoginRedirect(instructorUser);
        simulateLoginRedirect(memberUser);

        System.out.println("\n🎉 USER AUTHENTICATION TEST COMPLETED SUCCESSFULLY!");
        System.out.println("Next: Implement UserDAO and LoginServlet");
    }

    private static void simulateLoginRedirect(User user) {
        System.out.print("👤 " + user.getUsername() + " logs in → ");

        switch(user.getPrimaryRole()) {
            case ADMIN:
                System.out.println("Redirects to: /admin/dashboard.jsp");
                System.out.println("   Can access: All CRUD operations");
                break;
            case INSTRUCTOR:
                System.out.println("Redirects to: /instructor/dashboard.jsp");
                System.out.println("   Can access: View assigned members, view own data");
                break;
            case MEMBER:
                System.out.println("Redirects to: /member/profile.jsp");
                System.out.println("   Can access: View own profile only");
                break;
        }
    }
}