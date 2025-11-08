package com.karateclub.service;

import com.karateclub.model.BeltTest;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BeltTestService {

    // Basic CRUD operations
    BeltTest getTestById(int testId);
    List<BeltTest> getAllTests();
    BeltTest createTest(BeltTest test);
    BeltTest updateTest(BeltTest test);
    void deleteTest(int testId);

    // Member-specific operations
    List<BeltTest> getTestsByMember(int memberId);
    List<BeltTest> getTestsByMemberAndRank(int memberId, int rankId);
    BeltTest getLatestTestByMember(int memberId);

    // Rank-specific operations
    List<BeltTest> getTestsByRank(int rankId);
    List<BeltTest> getPassedTestsByRank(int rankId);

    // Instructor-specific operations
    List<BeltTest> getTestsByInstructor(int instructorId);

    // Business operations
    BeltTest scheduleTest(int memberId, int rankId, int instructorId, LocalDate date);
    BeltTest recordTestResult(int testId, boolean result, int paymentId);
    boolean isMemberEligibleForTest(int memberId, int rankId);

    // Reporting
    List<BeltTest> getTestsByDateRange(LocalDate startDate, LocalDate endDate);
    List<BeltTest> getUpcomingTests(int daysAhead);
    int getPassRateByRank(int rankId);
    Map<Integer, Integer> getTestStatisticsByInstructor(int instructorId);
}