package com.karateclub.service;

import com.karateclub.model.Payment;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PaymentService {

    // Basic CRUD operations
    Payment getPaymentById(int paymentId);
    List<Payment> getAllPayments();
    Payment createPayment(Payment payment);
    Payment updatePayment(Payment payment);
    void deletePayment(int paymentId);

    // Member-specific operations
    List<Payment> getPaymentsByMember(int memberId);
    List<Payment> getPaymentsByMemberAndDateRange(int memberId, LocalDate startDate, LocalDate endDate);

    // Business operations - CRITICAL for MemberService integration
    Payment processPayment(int memberId, double amount, LocalDate date);
    boolean hasUnpaidFees(int memberId); // ← Directly supports MemberServiceImpl
    double calculateMemberBalance(int memberId);
    double calculateTotalOwed(int memberId);

    // Reporting and analytics
    List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate);
    double calculateTotalRevenue(LocalDate startDate, LocalDate endDate);
    Map<LocalDate, Double> getDailyRevenue(LocalDate startDate, LocalDate endDate);
    Map<Integer, Double> getRevenueByMember();

    // Utility methods
    double calculateSubscriptionFee(int memberId);
    double calculateTestFee(int memberId, int beltRankId);
}