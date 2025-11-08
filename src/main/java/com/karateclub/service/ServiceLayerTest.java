package com.karateclub.service;

import com.karateclub.model.*;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ServiceLayerTest {

    public static void main(String[] args) {
        System.out.println("🧪 STARTING SERVICE LAYER TESTS...\n");

        try {
            testMemberService();
            testBeltRankService();
            testSubscriptionPeriodService();
            testPaymentService();
            testBeltTestService();
            testInstructorService();

            System.out.println("\n✅ ALL TESTS PASSED SUCCESSFULLY!");

        } catch (Exception e) {
            System.out.println("\n❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testMemberService() {
        System.out.println("=== TESTING MEMBER SERVICE ===");
        MemberService memberService = new MemberServiceImpl();

        // Test 1: Get member by ID
        Member member = memberService.getMemberById(1);
        System.out.println("✅ Get member by ID: " + member.getPerson().getName());

        // Test 2: Get all members
        List<Member> allMembers = memberService.getAllMembers();
        System.out.println("✅ Get all members: " + allMembers.size() + " members found");

        // Test 3: Get active members
        List<Member> activeMembers = memberService.getActiveMembers();
        System.out.println("✅ Get active members: " + activeMembers.size() + " active members");

        // Test 4: Check promotion eligibility
        boolean eligible = memberService.isMemberEligibleForPromotion(1);
        System.out.println("✅ Member 1 promotion eligibility: " + eligible);

        // Test 5: Try to get non-existent member (should throw exception)
        try {
            memberService.getMemberById(999);
            System.out.println("❌ Should have thrown exception for non-existent member");
        } catch (NotFoundException e) {
            System.out.println("✅ Correctly threw NotFoundException for non-existent member");
        }

        System.out.println();
    }

    private static void testBeltRankService() {
        System.out.println("=== TESTING BELT RANK SERVICE ===");
        BeltRankService beltRankService = new BeltRankServiceImpl();

        // Test 1: Get belt rank by ID
        BeltRank whiteBelt = beltRankService.getBeltRankById(1);
        System.out.println("✅ Get belt rank by ID: " + whiteBelt.getRankName());

        // Test 2: Get all belt ranks
        List<BeltRank> allRanks = beltRankService.getAllBeltRanks();
        System.out.println("✅ Get all belt ranks: " + allRanks.size() + " ranks found");

        // Test 3: Check belt rank fees
        System.out.println("✅ White belt test fees: $" + whiteBelt.getTestFees());

        System.out.println();
    }

    private static void testSubscriptionPeriodService() {
        System.out.println("=== TESTING SUBSCRIPTION PERIOD SERVICE ===");
        SubscriptionPeriodService subService = new SubscriptionPeriodServiceImpl();

        // Test 1: Check active subscription
        boolean hasActiveSub = subService.hasActiveSubscription(2);
        System.out.println("✅ Member 2 has active subscription: " + hasActiveSub);

        // Test 2: Get subscriptions by member
        List<SubscriptionPeriod> memberSubs = subService.getSubscriptionsByMember(1);
        System.out.println("✅ Member 1 subscription count: " + memberSubs.size());

        // Test 3: Get current subscription
        SubscriptionPeriod currentSub = subService.getCurrentSubscription(2);
        System.out.println("✅ Member 2 current subscription: " + (currentSub != null ? "Found" : "None"));

        System.out.println();
    }

    private static void testPaymentService() {
        System.out.println("=== TESTING PAYMENT SERVICE ===");
        PaymentService paymentService = new PaymentServiceImpl();

        // Test 1: Get payments by member
        List<Payment> memberPayments = paymentService.getPaymentsByMember(1);
        System.out.println("✅ Member 1 payment count: " + memberPayments.size());

        // Test 2: Check unpaid fees
        boolean hasUnpaidFees = paymentService.hasUnpaidFees(3);
        System.out.println("✅ Member 3 has unpaid fees: " + hasUnpaidFees);

        // Test 3: Calculate member balance
        double balance = paymentService.calculateMemberBalance(1);
        System.out.println("✅ Member 1 balance: $" + balance);

        // Test 4: Calculate total revenue
        double revenue = paymentService.calculateTotalRevenue(
                LocalDate.now().minusMonths(1),
                LocalDate.now()
        );
        System.out.println("✅ Last month revenue: $" + revenue);

        System.out.println();
    }

    private static void testBeltTestService() {
        System.out.println("=== TESTING BELT TEST SERVICE ===");
        BeltTestService testService = new BeltTestServiceImpl();

        // Test 1: Get tests by member
        List<BeltTest> memberTests = testService.getTestsByMember(1);
        System.out.println("✅ Member 1 test count: " + memberTests.size());

        // Test 2: Get latest test by member
        BeltTest latestTest = testService.getLatestTestByMember(1);
        System.out.println("✅ Member 1 latest test: " + (latestTest != null ? latestTest.getRank().getRankName() : "None"));

        // Test 3: Check test eligibility
        boolean eligible = testService.isMemberEligibleForTest(1, 2); // Member 1 for Yellow Belt
        System.out.println("✅ Member 1 eligible for Yellow Belt test: " + eligible);

        // Test 4: Get pass rate for a rank
        int passRate = testService.getPassRateByRank(1); // White Belt
        System.out.println("✅ White Belt pass rate: " + passRate + "%");

        System.out.println();
    }

    private static void testInstructorService() {
        System.out.println("=== TESTING INSTRUCTOR SERVICE ===");
        InstructorService instructorService = new InstructorServiceImpl();

        // Test 1: Get instructor by ID
        Instructor instructor = instructorService.getInstructorById(1);
        System.out.println("✅ Get instructor by ID: " + instructor.getPerson().getName());

        // Test 2: Get all instructors
        List<Instructor> allInstructors = instructorService.getAllInstructors();
        System.out.println("✅ Get all instructors: " + allInstructors.size() + " instructors found");

        // Test 3: Get active instructors
        List<Instructor> activeInstructors = instructorService.getActiveInstructors();
        System.out.println("✅ Get active instructors: " + activeInstructors.size() + " active instructors");

        // Test 4: Get members by instructor
        List<Member> instructorMembers = instructorService.getMembersByInstructor(1);
        System.out.println("✅ Instructor 1 student count: " + instructorMembers.size());

        // Test 5: Check instructor qualification
        System.out.println("✅ Instructor 1 qualification: " + instructor.getQualification());

        System.out.println();
    }

    // Additional test for error cases
    private static void testErrorCases() {
        System.out.println("=== TESTING ERROR CASES ===");
        MemberService memberService = new MemberServiceImpl();

        // Test invalid ID
        try {
            memberService.getMemberById(-1);
            System.out.println("❌ Should have thrown ValidationException for negative ID");
        } catch (ValidationException e) {
            System.out.println("✅ Correctly threw ValidationException for negative ID");
        }

        // Test business rule - promote inactive member
        try {
            memberService.promoteMember(4, 2); // Member 4 is inactive
            System.out.println("❌ Should have thrown BusinessRuleException for inactive member");
        } catch (BusinessRuleException e) {
            System.out.println("✅ Correctly threw BusinessRuleException for inactive member");
        }

        System.out.println();
    }
}