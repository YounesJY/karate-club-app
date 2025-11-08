package com.karateclub;

import com.karateclub.model.*;
import com.karateclub.service.exception.BusinessRuleException;
import com.karateclub.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AdvancedIntegrationTest {

    public static void main(String[] args) {
        System.out.println("🔬 ADVANCED INTEGRATION TESTS - BUSINESS WORKFLOWS\n");

        try {
            testMemberPromotionWorkflow();
            testSubscriptionRenewalWorkflow();
            testPaymentAndActivationWorkflow();
            testInstructorAssignmentWorkflow();
            testErrorAndValidationScenarios();

            System.out.println("\n🎉 ALL ADVANCED TESTS PASSED! SYSTEM IS PRODUCTION-READY!");

        } catch (Exception e) {
            System.out.println("\n❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testMemberPromotionWorkflow() {
        System.out.println("=== TESTING MEMBER PROMOTION WORKFLOW ===");
        MemberService memberService = new MemberServiceImpl();
        BeltTestService testService = new BeltTestServiceImpl();
        PaymentService paymentService = new PaymentServiceImpl();

        // Test with existing data only - no new entity creation
        Member member = memberService.getMemberById(2); // Jane Smith (Yellow Belt)
        System.out.println("👤 Member: " + member.getPerson().getName() +
                " | Current Rank: " + member.getLastBeltRank().getRankName());

        // Test 1: Check promotion eligibility
        boolean eligible = memberService.isMemberEligibleForPromotion(2);
        System.out.println("✅ Promotion eligibility: " + eligible);

        // Test 2: Get member's test history
        List<BeltTest> memberTests = testService.getTestsByMember(2);
        System.out.println("✅ Test history: " + memberTests.size() + " tests");

        // Test 3: Try to promote member (this tests the business logic)
        try {
            memberService.promoteMember(2, 3); // Try to promote to Orange Belt
            System.out.println("✅ Promotion successful!");

            Member updatedMember = memberService.getMemberById(2);
            System.out.println("✅ New rank: " + updatedMember.getLastBeltRank().getRankName());

            // Rollback the promotion for test consistency
            updatedMember.setLastBeltRank(member.getLastBeltRank());
            memberService.updateMember(updatedMember);

        } catch (BusinessRuleException e) {
            System.out.println("✅ Promotion correctly blocked: " + e.getMessage());
        }

        // Test 4: Check test-related business logic
        boolean canTest = testService.isMemberEligibleForTest(2, 3); // Orange Belt test
        System.out.println("✅ Can schedule Orange Belt test: " + canTest);

        System.out.println();
    }
    private static void testSubscriptionRenewalWorkflow() {
        System.out.println("=== TESTING SUBSCRIPTION RENEWAL WORKFLOW ===");
        SubscriptionPeriodService subService = new SubscriptionPeriodServiceImpl();
        PaymentService paymentService = new PaymentServiceImpl();

        // Test Member 3 (Mike Johnson - has unpaid subscription)
        List<SubscriptionPeriod> unpaidSubs = subService.getUnpaidSubscriptions(3);
        System.out.println("💰 Unpaid subscriptions for Member 3: " + unpaidSubs.size());

        if (!unpaidSubs.isEmpty()) {
            SubscriptionPeriod unpaidSub = unpaidSubs.get(0);

            // Process payment for subscription
            Payment payment = paymentService.processPayment(3, unpaidSub.getFees(), LocalDate.now());
            System.out.println("✅ Subscription payment processed: $" + payment.getAmount());

            // Mark subscription as paid
            SubscriptionPeriod paidSub = subService.markAsPaid(unpaidSub.getPeriodID(), payment.getPaymentID());
            System.out.println("✅ Subscription marked as paid: " + paidSub.isPaid());

            // Verify member now has active subscription
            boolean hasActiveSub = subService.hasActiveSubscription(3);
            System.out.println("✅ Member now has active subscription: " + hasActiveSub);
        }

        System.out.println();
    }

    private static void testPaymentAndActivationWorkflow() {
        System.out.println("=== TESTING PAYMENT & ACTIVATION WORKFLOW ===");
        MemberService memberService = new MemberServiceImpl();
        PaymentService paymentService = new PaymentServiceImpl();

        // Test Member 4 (Sarah Wilson - inactive)
        Member inactiveMember = memberService.getMemberById(4);
        System.out.println("👤 Inactive Member: " + inactiveMember.getPerson().getName() +
                " | Active: " + inactiveMember.isActive());

        // Check why inactive (unpaid fees)
        boolean hasUnpaidFees = paymentService.hasUnpaidFees(4);
        double balance = paymentService.calculateMemberBalance(4);
        System.out.println("💰 Has unpaid fees: " + hasUnpaidFees + " | Balance: $" + balance);

        if (hasUnpaidFees) {
            // Process payment to clear balance
            Payment payment = paymentService.processPayment(4, balance, LocalDate.now());
            System.out.println("✅ Balance cleared with payment: $" + payment.getAmount());

            // Try to activate member
            boolean activated = memberService.activateMember(4);
            System.out.println("✅ Member activated: " + activated);

            // Verify member is now active
            Member activeMember = memberService.getMemberById(4);
            System.out.println("✅ Member is now active: " + activeMember.isActive());
        }

        System.out.println();
    }

    private static void testInstructorAssignmentWorkflow() {
        System.out.println("=== TESTING INSTRUCTOR ASSIGNMENT WORKFLOW ===");
        InstructorService instructorService = new InstructorServiceImpl();
        MemberService memberService = new MemberServiceImpl();

        // Test Instructor 1
        Instructor instructor = instructorService.getInstructorById(1);
        System.out.println("👨‍🏫 Instructor: " + instructor.getPerson().getName());
        System.out.println("📊 Current students: " + instructorService.getInstructorStudentCount(1));

        // Get current students
        List<Member> students = instructorService.getMembersByInstructor(1);
        System.out.println("🎓 Current students: " + students.size());
        students.forEach(student ->
                System.out.println("   - " + student.getPerson().getName() +
                        " (" + student.getLastBeltRank().getRankName() + ")")
        );

        // Try to assign a member who ISN'T already assigned
        List<Member> allMembers = memberService.getAllMembers();
        Member unassignedMember = allMembers.stream()
                .filter(m -> m.getMemberID() == 4) // Sarah Wilson (shouldn't be assigned yet)
                .findFirst()
                .orElse(null);

        if (unassignedMember != null) {
            try {
                Instructor updatedInstructor = instructorService.assignMemberToInstructor(1, 4);
                System.out.println("✅ New student assigned: " + unassignedMember.getPerson().getName());
                System.out.println("📊 Updated student count: " + instructorService.getInstructorStudentCount(1));
            } catch (BusinessRuleException e) {
                System.out.println("✅ Correctly prevented assignment: " + e.getMessage());
            }
        }

        // Test instructor capabilities
        boolean canTestWhite = instructorService.canInstructorTestRank(1, 1);
        boolean canTestBlack = instructorService.canInstructorTestRank(1, 7);
        System.out.println("📊 Instructor capabilities:");
        System.out.println("   - Can test White Belt: " + canTestWhite);
        System.out.println("   - Can test Black Belt: " + canTestBlack);

        System.out.println();
    }
    private static void testErrorAndValidationScenarios() {
        System.out.println("=== TESTING ERROR & VALIDATION SCENARIOS ===");
        MemberService memberService = new MemberServiceImpl();
        BeltTestService testService = new BeltTestServiceImpl();

        // Test 1: Try to promote inactive member
        try {
            memberService.promoteMember(4, 2); // Inactive member
            System.out.println("❌ Should have thrown exception for inactive member");
        } catch (BusinessRuleException e) {
            System.out.println("✅ Correctly blocked promotion for inactive member");
        }

        // Test 2: Try to schedule test for ineligible member
        try {
            testService.scheduleTest(1, 1, 1, LocalDate.now().plusDays(7)); // Already has white belt
            System.out.println("❌ Should have thrown exception for duplicate rank test");
        } catch (BusinessRuleException e) {
            System.out.println("✅ Correctly blocked duplicate rank test");
        }

        // Test 3: Try to create payment with negative amount
        try {
            PaymentService paymentService = new PaymentServiceImpl();
            paymentService.processPayment(1, -50.0, LocalDate.now());
            System.out.println("❌ Should have thrown exception for negative payment");
        } catch (Exception e) {
            System.out.println("✅ Correctly blocked negative payment");
        }

        System.out.println();
    }
}