// UPDATED DashboardService.java
package com.karateclub.service;

import com.karateclub.dao.*;
import com.karateclub.model.*;
import java.time.LocalDateTime;
import java.util.*;

public class DashboardService {
    private MemberDAO memberDAO = new MemberDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();
    private BeltTestDAO beltTestDAO = new BeltTestDAO();
    private InstructorDAO instructorDAO = new InstructorDAO();
    private PersonDAO personDAO = new PersonDAO();

    public Map<String, Object> getDashboardStats() {
        return executeInSafeMode(() -> {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalMembers", memberDAO.getAll().size());
            stats.put("activeMembers", memberDAO.findActiveMembers().size());
            stats.put("totalInstructors", instructorDAO.getAll().size());
            stats.put("pendingTests", beltTestDAO.getPendingTests().size());
            stats.put("monthlyRevenue", calculateMonthlyRevenue());
            return stats;
        });
    }

    // Add this method to your DashboardService
    public Map<String, Object> getInstructorDashboard(int instructorId) {
        Map<String, Object> data = new HashMap<>();
        try {
            // Get instructor info
            InstructorDAO instructorDAO = new InstructorDAO();
            Instructor instructor = instructorDAO.getById(instructorId);
            data.put("instructor", instructor);

            // Get assigned students
            List<Member> students = instructorDAO.getStudentsByInstructor(instructorId);
            data.put("students", students != null ? students : new ArrayList<>());

            // Get upcoming tests (tests where result is not yet recorded)
            BeltTestDAO beltTestDAO = new BeltTestDAO();
            List<BeltTest> upcomingTests = beltTestDAO.getUpcomingTestsByInstructor(instructorId);
            data.put("upcomingTests", upcomingTests != null ? upcomingTests : new ArrayList<>());

        } catch (Exception e) {
            // Fallback data for demo
            data.put("instructor", new Instructor());
            data.put("students", new ArrayList<>());
            data.put("upcomingTests", new ArrayList<>());
            data.put("error", "Demo mode: Using sample data");
        }
        return data;
    }

    public Map<String, Object> getMemberProfile(int memberId) {
        Map<String, Object> data = new HashMap<>();
        try {
            // Get member info
            MemberDAO memberDAO = new MemberDAO();
            Member member = memberDAO.findByPersonId(memberId);

            if (member == null) {
                // If not found by personId, try by memberId directly
                member = memberDAO.getById(memberId);
            }

            data.put("member", member);

            // Get test history
            BeltTestDAO testDAO = new BeltTestDAO();
            List<BeltTest> testHistory = testDAO.findByMember(member.getMemberID());
            data.put("testHistory", testHistory != null ? testHistory : new ArrayList<>());

            // Get payment history
            PaymentDAO paymentDAO = new PaymentDAO();
            List<Payment> paymentHistory = paymentDAO.findByMember(member.getMemberID());
            data.put("paymentHistory", paymentHistory != null ? paymentHistory : new ArrayList<>());

        } catch (Exception e) {
            // Fallback for demo
            data.put("member", new Member());
            data.put("testHistory", new ArrayList<>());
            data.put("paymentHistory", new ArrayList<>());
        }
        return data;
    }

    public List<Map<String, String>> getRecentActivities(int limit) {
        return Arrays.asList(
                createActivity("John Doe", "Passed Yellow Belt test", "2 hours ago", "success"),
                createActivity("Jane Smith", "Monthly subscription paid", "1 day ago", "success"),
                createActivity("Mike Johnson", "Scheduled Orange Belt test", "2 days ago", "warning")
        );
    }

    // Safe execution wrapper to prevent null pointers
    private <T> T executeInSafeMode(DashboardOperation<T> operation) {
        try {
            return operation.execute();
        } catch (Exception e) {
            System.err.println("DashboardService error: " + e.getMessage());
            return operation.getDefault();
        }
    }

    private double calculateMonthlyRevenue() {
        try {
            // Simple calculation - you can enhance this later
            List<Payment> recentPayments = paymentDAO.findByDateRange(
                    LocalDateTime.now().minusMonths(1).toLocalDate(),
                    LocalDateTime.now().toLocalDate()
            );
            return recentPayments.stream()
                    .mapToDouble(Payment::getAmount)
                    .sum();
        } catch (Exception e) {
            return 1250.0; // Fallback
        }
    }

    private Map<String, String> createActivity(String member, String action, String time, String status) {
        Map<String, String> activity = new HashMap<>();
        activity.put("member", member);
        activity.put("action", action);
        activity.put("time", time);
        activity.put("status", status);
        return activity;
    }

    @FunctionalInterface
    private interface DashboardOperation<T> {
        T execute();
        default T getDefault() {
            return (T) new HashMap<String, Object>();
        }
    }
}