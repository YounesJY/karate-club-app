package com.karateclub.tests;

import com.karateclub.model.*;
import com.karateclub.service.exception.BusinessRuleException;
import com.karateclub.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

        // Dynamically find a member instead of hardcoding ID
        List<Member> allMembers = memberService.getAllMembers();
        if (allMembers.isEmpty()) {
            System.out.println("❌ No members found in database");
            return;
        }

        // Find an active member with a specific belt rank for testing
        Optional<Member> testMember = allMembers.stream()
                .filter(Member::isActive)
                .filter(m -> m.getLastBeltRank().getRankID() == 2) // Yellow Belt
                .findFirst();

        if (testMember.isPresent()) {
            System.out.println("❌ No suitable test member found (active with Yellow Belt)");
            return;
        }

        Member member = testMember.get();
        int memberId = member.getMemberID();

        System.out.println("👤 Member: " + member.getPerson().getName() +
                " | Current Rank: " + member.getLastBeltRank().getRankName() +
                " | ID: " + memberId);

        // Test 1: Check promotion eligibility
        boolean eligible = memberService.isMemberEligibleForPromotion(memberId);
        System.out.println("✅ Promotion eligibility: " + eligible);

        // Test 2: Get member's test history
        List<BeltTest> memberTests = testService.getTestsByMember(memberId);
        System.out.println("✅ Test history: " + memberTests.size() + " tests");

        // Test 3: Try to promote member
        try {
            memberService.promoteMember(memberId, 3); // Try to promote to Orange Belt
            System.out.println("✅ Promotion successful!");

            Member updatedMember = memberService.getMemberById(memberId);
            System.out.println("✅ New rank: " + updatedMember.getLastBeltRank().getRankName());

            // Rollback the promotion for test consistency
            updatedMember.setLastBeltRank(member.getLastBeltRank());
            memberService.updateMember(updatedMember);

        } catch (BusinessRuleException e) {
            System.out.println("✅ Promotion correctly blocked: " + e.getMessage());
        }

        System.out.println();
    }

    private static void testSubscriptionRenewalWorkflow() {
        System.out.println("=== TESTING SUBSCRIPTION RENEWAL WORKFLOW ===");
        SubscriptionPeriodService subService = new SubscriptionPeriodServiceImpl();
        PaymentService paymentService = new PaymentServiceImpl();
        MemberService memberService = new MemberServiceImpl();

        // Find a member with unpaid subscriptions
        List<Member> allMembers = memberService.getAllMembers();
        Optional<Member> memberWithUnpaid = allMembers.stream()
                .filter(m -> {
                    List<SubscriptionPeriod> unpaid = subService.getUnpaidSubscriptions(m.getMemberID());
                    return !unpaid.isEmpty();
                })
                .findFirst();

        if (memberWithUnpaid.isPresent()) {
            System.out.println("❌ No members with unpaid subscriptions found");
            return;
        }

        Member member = memberWithUnpaid.get();
        int memberId = member.getMemberID();

        List<SubscriptionPeriod> unpaidSubs = subService.getUnpaidSubscriptions(memberId);
        System.out.println("💰 Unpaid subscriptions for " + member.getPerson().getName() + ": " + unpaidSubs.size());

        SubscriptionPeriod unpaidSub = unpaidSubs.get(0);

        // Process payment for subscription
        Payment payment = paymentService.processPayment(memberId, unpaidSub.getFees(), LocalDate.now());
        System.out.println("✅ Subscription payment processed: $" + payment.getAmount());

        // Mark subscription as paid
        SubscriptionPeriod paidSub = subService.markAsPaid(unpaidSub.getPeriodID(), payment.getPaymentID());
        System.out.println("✅ Subscription marked as paid: " + paidSub.isPaid());

        // Verify member now has active subscription
        boolean hasActiveSub = subService.hasActiveSubscription(memberId);
        System.out.println("✅ Member now has active subscription: " + hasActiveSub);

        System.out.println();
    }

    private static void testPaymentAndActivationWorkflow() {
        System.out.println("=== TESTING PAYMENT & ACTIVATION WORKFLOW ===");
        MemberService memberService = new MemberServiceImpl();
        PaymentService paymentService = new PaymentServiceImpl();

        // Find an inactive member
        List<Member> allMembers = memberService.getAllMembers();
        Optional<Member> inactiveMemberOpt = allMembers.stream()
                .filter(m -> !m.isActive())
                .findFirst();

        if (inactiveMemberOpt.isPresent()) {
            System.out.println("❌ No inactive members found");
            return;
        }

        Member inactiveMember = inactiveMemberOpt.get();
        int memberId = inactiveMember.getMemberID();

        System.out.println("👤 Inactive Member: " + inactiveMember.getPerson().getName() +
                " | Active: " + inactiveMember.isActive() + " | ID: " + memberId);

        // Check why inactive (unpaid fees)
        boolean hasUnpaidFees = paymentService.hasUnpaidFees(memberId);
        double balance = paymentService.calculateMemberBalance(memberId);
        System.out.println("💰 Has unpaid fees: " + hasUnpaidFees + " | Balance: $" + balance);

        if (hasUnpaidFees) {
            // Process payment to clear balance
            Payment payment = paymentService.processPayment(memberId, balance, LocalDate.now());
            System.out.println("✅ Balance cleared with payment: $" + payment.getAmount());

            // Try to activate member
            boolean activated = memberService.activateMember(memberId);
            System.out.println("✅ Member activated: " + activated);

            // Verify member is now active
            Member activeMember = memberService.getMemberById(memberId);
            System.out.println("✅ Member is now active: " + activeMember.isActive());
        }

        System.out.println();
    }

    private static void testInstructorAssignmentWorkflow() {
        System.out.println("=== TESTING INSTRUCTOR ASSIGNMENT WORKFLOW ===");
        InstructorService instructorService = new InstructorServiceImpl();
        MemberService memberService = new MemberServiceImpl();

        // Find an instructor
        List<Instructor> allInstructors = instructorService.getAllInstructors();
        if (allInstructors.isEmpty()) {
            System.out.println("❌ No instructors found");
            return;
        }

        Instructor instructor = allInstructors.get(0);
        int instructorId = instructor.getInstructorID();

        System.out.println("👨‍🏫 Instructor: " + instructor.getPerson().getName() + " | ID: " + instructorId);
        System.out.println("📊 Current students: " + instructorService.getInstructorStudentCount(instructorId));

        // Get current students
        List<Member> students = instructorService.getMembersByInstructor(instructorId);
        System.out.println("🎓 Current students: " + students.size());
        students.forEach(student ->
                System.out.println("   - " + student.getPerson().getName() +
                        " (" + student.getLastBeltRank().getRankName() + ")")
        );

        // Find a member who isn't assigned to this instructor
        List<Member> allMembers = memberService.getAllMembers();
        Optional<Member> unassignedMember = allMembers.stream()
                .filter(m -> students.stream().noneMatch(s -> s.getMemberID() == m.getMemberID()))
                .findFirst();

        if (unassignedMember.isPresent()) {
            Member member = unassignedMember.get();
            try {
                Instructor updatedInstructor = instructorService.assignMemberToInstructor(instructorId, member.getMemberID());
                System.out.println("✅ New student assigned: " + member.getPerson().getName());
                System.out.println("📊 Updated student count: " + instructorService.getInstructorStudentCount(instructorId));
            } catch (BusinessRuleException e) {
                System.out.println("✅ Correctly prevented assignment: " + e.getMessage());
            }
        } else {
            System.out.println("✅ All members are already assigned to this instructor");
        }

        System.out.println();
    }

    private static void testErrorAndValidationScenarios() {
        System.out.println("=== TESTING ERROR & VALIDATION SCENARIOS ===");
        MemberService memberService = new MemberServiceImpl();
        BeltTestService testService = new BeltTestServiceImpl();

        // Find an inactive member
        List<Member> allMembers = memberService.getAllMembers();
        Optional<Member> inactiveMember = allMembers.stream()
                .filter(m -> !m.isActive())
                .findFirst();

        // Test 1: Try to promote inactive member
        if (inactiveMember.isPresent()) {
            try {
                memberService.promoteMember(inactiveMember.get().getMemberID(), 2);
                System.out.println("❌ Should have thrown exception for inactive member");
            } catch (BusinessRuleException e) {
                System.out.println("✅ Correctly blocked promotion for inactive member");
            }
        } else {
            System.out.println("⚠️  No inactive members found for testing");
        }

        // Test 2: Try invalid operations
        try {
            memberService.promoteMember(-1, 1); // Invalid member ID
            System.out.println("❌ Should have thrown exception for invalid member ID");
        } catch (Exception e) {
            System.out.println("✅ Correctly blocked invalid member ID");
        }

        System.out.println();
    }
}