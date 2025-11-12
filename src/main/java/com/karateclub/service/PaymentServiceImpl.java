package com.karateclub.service;

import com.karateclub.dao.PaymentDAO;
import com.karateclub.dao.MemberDAO;
import com.karateclub.dao.BeltRankDAO;
import com.karateclub.dao.SubscriptionPeriodDAO;
import com.karateclub.dao.BeltTestDAO;
import com.karateclub.model.Payment;
import com.karateclub.model.Member;
import com.karateclub.model.BeltRank;
import com.karateclub.model.SubscriptionPeriod;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentServiceImpl implements PaymentService {
    private PaymentDAO paymentDAO;
    private MemberDAO memberDAO;
    private BeltRankDAO beltRankDAO;
    private SubscriptionPeriodDAO subscriptionPeriodDAO;
    private BeltTestDAO beltTestDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAO();
        this.memberDAO = new MemberDAO();
        this.beltRankDAO = new BeltRankDAO();
        this.subscriptionPeriodDAO = new SubscriptionPeriodDAO();
        this.beltTestDAO = new BeltTestDAO();
    }

    public PaymentServiceImpl(PaymentDAO paymentDAO, MemberDAO memberDAO,
                              BeltRankDAO beltRankDAO, SubscriptionPeriodDAO subscriptionPeriodDAO,
                              BeltTestDAO beltTestDAO) {
        this.paymentDAO = paymentDAO;
        this.memberDAO = memberDAO;
        this.beltRankDAO = beltRankDAO;
        this.subscriptionPeriodDAO = subscriptionPeriodDAO;
        this.beltTestDAO = beltTestDAO;
    }

    @Override
    public Payment getPaymentById(int paymentId) {
        validatePaymentId(paymentId);

        Payment payment = paymentDAO.getById(paymentId);
        if (payment == null) {
            throw new NotFoundException("Payment not found with ID: " + paymentId);
        }
        return payment;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentDAO.getAll();
    }

    @Override
    public Payment createPayment(Payment payment) {
        validatePayment(payment);

        // Business rule: Payment date cannot be in the future
        if (payment.getDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Payment date cannot be in the future");
        }

        // Business rule: Amount must be positive
        if (payment.getAmount() <= 0) {
            throw new ValidationException("Payment amount must be positive");
        }

        paymentDAO.save(payment);

        // Automatically create or extend subscription period when payment is made
        createOrExtendSubscription(payment);

        return payment;
    }

    /**
     * Creates or extends a subscription period when a payment is made
     */
    private void createOrExtendSubscription(Payment payment) {
        try {
            Member member = payment.getMember();
            if (member == null) {
                return; // No member associated with payment
            }

            int memberId = member.getMemberID();

            // Check if member has any subscription (active or expired)
            SubscriptionPeriod latestSubscription = subscriptionPeriodDAO.getLatestSubscription(memberId);

            LocalDateTime startDate;
            LocalDateTime endDate;

            if (latestSubscription != null && latestSubscription.getEndDate().isAfter(LocalDateTime.now())) {
                // Extend existing active subscription
                startDate = latestSubscription.getEndDate();
                // Add 30 days (1 month) for every 100 units of payment
                int daysToAdd = (int) ((payment.getAmount() / 100.0) * 30);
                if (daysToAdd < 30) {
                    daysToAdd = 30; // Minimum 1 month
                }
                endDate = startDate.plusDays(daysToAdd);
            } else {
                // Create new subscription (if no subscription or if expired)
                startDate = LocalDateTime.now();
                // Add 30 days (1 month) for every 100 units of payment
                int daysToAdd = (int) ((payment.getAmount() / 100.0) * 30);
                if (daysToAdd < 30) {
                    daysToAdd = 30; // Minimum 1 month
                }
                endDate = startDate.plusDays(daysToAdd);
            }

            // Create new subscription period
            SubscriptionPeriod newSubscription = new SubscriptionPeriod();
            newSubscription.setMember(member);
            newSubscription.setStartDate(startDate);
            newSubscription.setEndDate(endDate);
            newSubscription.setFees(payment.getAmount());
            newSubscription.setPaid(true);
            newSubscription.setPayment(payment);

            subscriptionPeriodDAO.save(newSubscription);

        } catch (Exception e) {
            // Log error but don't fail the payment
            System.err.println("Error creating subscription period: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Payment updatePayment(Payment payment) {
        validatePayment(payment);
        validatePaymentExists(payment.getPaymentID());

        paymentDAO.update(payment);
        return payment;
    }

    @Override
    public void deletePayment(int paymentId) {
        validatePaymentId(paymentId);

        Payment payment = paymentDAO.getById(paymentId);
        if (payment != null) {
            // Business rule: Prevent deletion of payments older than 30 days
            if (payment.getDate().isBefore(LocalDate.now().minusDays(30))) {
                throw new BusinessRuleException("Cannot delete payments older than 30 days");
            }

            // Handle foreign key constraints: nullify payment references before deletion
            try {
                // Nullify in subscription periods
                subscriptionPeriodDAO.nullifyPaymentReference(paymentId);
            } catch (Exception e) {
                System.err.println("Warning: Could not nullify payment references in subscription periods: " + e.getMessage());
            }

            try {
                // Nullify in belt tests
                beltTestDAO.nullifyPaymentReference(paymentId);
            } catch (Exception e) {
                System.err.println("Warning: Could not nullify payment references in belt tests: " + e.getMessage());
            }

            // Now safe to delete the payment
            paymentDAO.delete(paymentId);
        } else {
            throw new NotFoundException("Payment not found with ID: " + paymentId);
        }
    }

    @Override
    public List<Payment> getPaymentsByMember(int memberId) {
        validateMemberId(memberId);
        return paymentDAO.findByMember(memberId);
    }

    @Override
    public List<Payment> getPaymentsByMemberAndDateRange(int memberId, LocalDate startDate, LocalDate endDate) {
        validateMemberId(memberId);
        validateDateRange(startDate, endDate);

        return paymentDAO.findByMemberAndDateRange(memberId, startDate, endDate);
    }

    @Override
    public Payment processPayment(int memberId, double amount, LocalDate date) {
        validateMemberId(memberId);

        if (amount <= 0) {
            throw new ValidationException("Payment amount must be positive");
        }
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new ValidationException("Payment date cannot be in the future");
        }

        Member member = memberDAO.getById(memberId);
        if (member == null) {
            throw new NotFoundException("Member not found with ID: " + memberId);
        }

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setDate(date != null ? date : LocalDate.now());
        payment.setMember(member);

        // This will automatically create/extend subscription via createPayment
        return createPayment(payment);
    }

    @Override
    public boolean hasUnpaidFees(int memberId) {
        validateMemberId(memberId);

        // This is the CRITICAL method that your MemberServiceImpl depends on
        // Business logic: A member has unpaid fees if their balance is positive
        double balance = calculateMemberBalance(memberId);
        return balance > 0;
    }

    @Override
    public double calculateMemberBalance(int memberId) {
        validateMemberId(memberId);

        double totalOwed = calculateTotalOwed(memberId);
        double totalPaid = paymentDAO.getTotalPaymentsByMember(memberId);

        return totalOwed - totalPaid;
    }

    @Override
    public double calculateTotalOwed(int memberId) {
        validateMemberId(memberId);

        // Calculate all fees owed by member
        double subscriptionFees = calculateSubscriptionFee(memberId);
        double testFees = calculateOutstandingTestFees(memberId);

        return subscriptionFees + testFees;
    }

    @Override
    public List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        return paymentDAO.findByDateRange(startDate, endDate);
    }

    @Override
    public double calculateTotalRevenue(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        List<Payment> payments = getPaymentsByDateRange(startDate, endDate);
        return payments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    @Override
    public Map<LocalDate, Double> getDailyRevenue(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        List<Payment> payments = getPaymentsByDateRange(startDate, endDate);
        return payments.stream()
                .collect(Collectors.groupingBy(
                        Payment::getDate,
                        Collectors.summingDouble(Payment::getAmount)
                ));
    }

    @Override
    public Map<Integer, Double> getRevenueByMember() {
        List<Payment> allPayments = getAllPayments();
        return allPayments.stream()
                .collect(Collectors.groupingBy(
                        payment -> payment.getMember().getMemberID(),
                        Collectors.summingDouble(Payment::getAmount)
                ));
    }

    @Override
    public double calculateSubscriptionFee(int memberId) {
        // Check for unpaid subscription periods
        List<SubscriptionPeriod> unpaidSubscriptions = subscriptionPeriodDAO.findUnpaidSubscriptions()
                .stream()
                .filter(sub -> sub.getMember().getMemberID() == memberId)
                .collect(Collectors.toList());

        return unpaidSubscriptions.stream()
                .mapToDouble(SubscriptionPeriod::getFees)
                .sum();
    }

    @Override
    public double calculateTestFee(int memberId, int beltRankId) {
        validateMemberId(memberId);
        validateBeltRankId(beltRankId);

        BeltRank beltRank = beltRankDAO.getById(beltRankId);
        if (beltRank == null) {
            throw new NotFoundException("Belt rank not found with ID: " + beltRankId);
        }

        return beltRank.getTestFees();
    }

    // Private helper methods
    private double calculateOutstandingTestFees(int memberId) {
        // This would check if member is eligible for promotion and has unpaid test fees
        // For now, return 0 - can be enhanced later
        return 0.0;
    }

    // Validation methods
    private void validatePaymentId(int paymentId) {
        if (paymentId <= 0) {
            throw new ValidationException("Payment ID must be positive");
        }
    }

    private void validateMemberId(int memberId) {
        if (memberId <= 0) {
            throw new ValidationException("Member ID must be positive");
        }
    }

    private void validateBeltRankId(int rankId) {
        if (rankId <= 0) {
            throw new ValidationException("Belt rank ID must be positive");
        }
    }

    private void validatePayment(Payment payment) {
        if (payment == null) {
            throw new ValidationException("Payment cannot be null");
        }
        if (payment.getAmount() <= 0) {
            throw new ValidationException("Payment amount must be positive");
        }
        if (payment.getDate() == null) {
            throw new ValidationException("Payment date is required");
        }
        if (payment.getMember() == null) {
            throw new ValidationException("Member is required");
        }
    }

    private void validatePaymentExists(int paymentId) {
        if (paymentDAO.getById(paymentId) == null) {
            throw new NotFoundException("Payment not found with ID: " + paymentId);
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