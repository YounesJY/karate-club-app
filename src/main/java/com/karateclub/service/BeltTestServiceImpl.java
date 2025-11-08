package com.karateclub.service;

import com.karateclub.dao.BeltTestDAO;
import com.karateclub.dao.MemberDAO;
import com.karateclub.dao.BeltRankDAO;
import com.karateclub.dao.InstructorDAO;
import com.karateclub.dao.PaymentDAO;
import com.karateclub.model.BeltTest;
import com.karateclub.model.Member;
import com.karateclub.model.BeltRank;
import com.karateclub.model.Instructor;
import com.karateclub.model.Payment;

import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeltTestServiceImpl implements BeltTestService {
    private BeltTestDAO beltTestDAO;
    private MemberDAO memberDAO;
    private BeltRankDAO beltRankDAO;
    private InstructorDAO instructorDAO;
    private PaymentDAO paymentDAO;

    public BeltTestServiceImpl() {
        this.beltTestDAO = new BeltTestDAO();
        this.memberDAO = new MemberDAO();
        this.beltRankDAO = new BeltRankDAO();
        this.instructorDAO = new InstructorDAO();
        this.paymentDAO = new PaymentDAO();
    }

    public BeltTestServiceImpl(BeltTestDAO beltTestDAO, MemberDAO memberDAO, BeltRankDAO beltRankDAO, InstructorDAO instructorDAO,  PaymentDAO paymentDAO) {
        this.beltTestDAO = beltTestDAO;
        this.memberDAO = memberDAO;
        this.beltRankDAO = beltRankDAO;
        this.instructorDAO = instructorDAO;
        this.paymentDAO = paymentDAO;
    }

    @Override
    public BeltTest getTestById(int testId) {
        validateTestId(testId);

        BeltTest test = beltTestDAO.getById(testId);
        if (test == null) {
            throw new NotFoundException("Belt test not found with ID: " + testId);
        }
        return test;
    }

    @Override
    public List<BeltTest> getAllTests() {
        return beltTestDAO.getAll();
    }

    @Override
    public BeltTest createTest(BeltTest test) {
        validateTest(test);

        // Business rule: Test date cannot be in the past
        if (test.getDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Test date cannot be in the past");
        }

        // Business rule: Member must be eligible for this test
        if (!isMemberEligibleForTest(test.getMember().getMemberID(), test.getRank().getRankID())) {
            throw new BusinessRuleException("Member is not eligible for this belt test");
        }

        beltTestDAO.save(test);
        return test;
    }

    @Override
    public BeltTest updateTest(BeltTest test) {
        validateTest(test);
        validateTestExists(test.getTestID());

        beltTestDAO.update(test);
        return test;
    }

    @Override
    public void deleteTest(int testId) {
        validateTestId(testId);

        BeltTest test = beltTestDAO.getById(testId);
        if (test != null) {
            // Business rule: Prevent deletion of tests that already happened
            if (test.getDate().isBefore(LocalDate.now())) {
                throw new BusinessRuleException("Cannot delete tests that have already occurred");
            }
            beltTestDAO.delete(testId);
        } else {
            throw new NotFoundException("Belt test not found with ID: " + testId);
        }
    }

    @Override
    public List<BeltTest> getTestsByMember(int memberId) {
        validateMemberId(memberId);
        return beltTestDAO.findByMember(memberId);
    }

    @Override
    public List<BeltTest> getTestsByMemberAndRank(int memberId, int rankId) {
        validateMemberId(memberId);
        validateRankId(rankId);

        // Using existing methods and filtering
        return getTestsByMember(memberId).stream()
                .filter(test -> test.getRank().getRankID() == rankId)
                .collect(Collectors.toList());
    }

    @Override
    public BeltTest getLatestTestByMember(int memberId) {
        validateMemberId(memberId);

        List<BeltTest> memberTests = getTestsByMember(memberId);
        return memberTests.stream()
                .max((t1, t2) -> t1.getDate().compareTo(t2.getDate()))
                .orElse(null);
    }

    @Override
    public List<BeltTest> getTestsByRank(int rankId) {
        validateRankId(rankId);
        // This would need a new DAO method, using filtering for now
        return getAllTests().stream()
                .filter(test -> test.getRank().getRankID() == rankId)
                .collect(Collectors.toList());
    }

    @Override
    public List<BeltTest> getPassedTestsByRank(int rankId) {
        validateRankId(rankId);
        return getTestsByRank(rankId).stream()
                .filter(BeltTest::isResult)
                .collect(Collectors.toList());
    }

    @Override
    public List<BeltTest> getTestsByInstructor(int instructorId) {
        validateInstructorId(instructorId);
        return beltTestDAO.findByInstructor(instructorId);
    }

    @Override
    public BeltTest scheduleTest(int memberId, int rankId, int instructorId, LocalDate date) {
        validateMemberId(memberId);
        validateRankId(rankId);
        validateInstructorId(instructorId);

        if (date == null || date.isBefore(LocalDate.now())) {
            throw new ValidationException("Test date must be in the future");
        }

        Member member = memberDAO.getById(memberId);
        BeltRank rank = beltRankDAO.getById(rankId);
        Instructor instructor = instructorDAO.getById(instructorId);

        if (member == null) throw new NotFoundException("Member not found with ID: " + memberId);
        if (rank == null) throw new NotFoundException("Belt rank not found with ID: " + rankId);
        if (instructor == null) throw new NotFoundException("Instructor not found with ID: " + instructorId);

        // Business rule: Check eligibility
        if (!isMemberEligibleForTest(memberId, rankId)) {
            throw new BusinessRuleException("Member is not eligible for this belt test");
        }

        BeltTest test = new BeltTest();
        test.setMember(member);
        test.setRank(rank);
        test.setTestedByInstructor(instructor);
        test.setDate(date);
        test.setResult(false); // Default to false until test is taken

        return createTest(test);
    }

    @Override
    public BeltTest recordTestResult(int testId, boolean result, int paymentId) {
        validateTestId(testId);
        validatePaymentId(paymentId);

        BeltTest test = getTestById(testId);
        Payment payment = paymentDAO.getById(paymentId);

        if (payment == null) {
            throw new NotFoundException("Payment not found with ID: " + paymentId);
        }

        // Business rule: Cannot record result for future tests
        if (test.getDate().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("Cannot record result for future tests");
        }

        test.setResult(result);
        test.setPayment(payment);

        // If test passed, update member's belt rank
        if (result) {
            Member member = test.getMember();
            member.setLastBeltRank(test.getRank());
            memberDAO.update(member);
        }

        return updateTest(test);
    }

    @Override
    public boolean isMemberEligibleForTest(int memberId, int rankId) {
        validateMemberId(memberId);
        validateRankId(rankId);

        Member member = memberDAO.getById(memberId);
        BeltRank targetRank = beltRankDAO.getById(rankId);

        if (member == null || !member.isActive()) {
            return false;
        }

        // Business rule: Member must have active subscription
        SubscriptionPeriodService subService = new SubscriptionPeriodServiceImpl();
        if (!subService.hasActiveSubscription(memberId)) {
            return false;
        }

        // Business rule: Member must have no unpaid fees
        PaymentService paymentService = new PaymentServiceImpl();
        if (paymentService.hasUnpaidFees(memberId)) {
            return false;
        }

        // Business rule: Target rank must be higher than current rank
        BeltRank currentRank = member.getLastBeltRank();
        if (currentRank != null && targetRank.getRankID() <= currentRank.getRankID()) {
            return false;
        }

        // Business rule: Check if member already passed this rank
        List<BeltTest> memberTests = getTestsByMemberAndRank(memberId, rankId);
        boolean alreadyPassed = memberTests.stream()
                .anyMatch(BeltTest::isResult);
        if (alreadyPassed) {
            return false;
        }

        return true;
    }

    @Override
    public List<BeltTest> getTestsByDateRange(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        return beltTestDAO.findByDateRange(startDate, endDate);
    }

    @Override
    public List<BeltTest> getUpcomingTests(int daysAhead) {
        if (daysAhead <= 0) {
            throw new ValidationException("Days ahead must be positive");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(daysAhead);
        return getTestsByDateRange(startDate, endDate);
    }

    @Override
    public int getPassRateByRank(int rankId) {
        validateRankId(rankId);

        List<BeltTest> rankTests = getTestsByRank(rankId);
        if (rankTests.isEmpty()) {
            return 0;
        }

        long passedTests = rankTests.stream()
                .filter(BeltTest::isResult)
                .count();

        return (int) ((passedTests * 100) / rankTests.size());
    }

    @Override
    public Map<Integer, Integer> getTestStatisticsByInstructor(int instructorId) {
        validateInstructorId(instructorId);

        List<BeltTest> instructorTests = getTestsByInstructor(instructorId);

        return instructorTests.stream()
                .collect(Collectors.groupingBy(
                        test -> test.getRank().getRankID(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                tests -> {
                                    long total = tests.size();
                                    long passed = tests.stream().filter(BeltTest::isResult).count();
                                    return total > 0 ? (int) ((passed * 100) / total) : 0;
                                }
                        )
                ));
    }

    // Validation methods
    private void validateTestId(int testId) {
        if (testId <= 0) {
            throw new ValidationException("Test ID must be positive");
        }
    }

    private void validateMemberId(int memberId) {
        if (memberId <= 0) {
            throw new ValidationException("Member ID must be positive");
        }
    }

    private void validateRankId(int rankId) {
        if (rankId <= 0) {
            throw new ValidationException("Rank ID must be positive");
        }
    }

    private void validateInstructorId(int instructorId) {
        if (instructorId <= 0) {
            throw new ValidationException("Instructor ID must be positive");
        }
    }

    private void validatePaymentId(int paymentId) {
        if (paymentId <= 0) {
            throw new ValidationException("Payment ID must be positive");
        }
    }

    private void validateTest(BeltTest test) {
        if (test == null) {
            throw new ValidationException("Belt test cannot be null");
        }
        if (test.getMember() == null) {
            throw new ValidationException("Member is required");
        }
        if (test.getRank() == null) {
            throw new ValidationException("Belt rank is required");
        }
        if (test.getTestedByInstructor() == null) {
            throw new ValidationException("Instructor is required");
        }
        if (test.getDate() == null) {
            throw new ValidationException("Test date is required");
        }
    }

    private void validateTestExists(int testId) {
        if (beltTestDAO.getById(testId) == null) {
            throw new NotFoundException("Belt test not found with ID: " + testId);
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ValidationException("Start date and end date are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date cannot be after end date");
        }
    }
}