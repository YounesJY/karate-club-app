package com.karateclub.service;

import com.karateclub.dao.SubscriptionPeriodDAO;
import com.karateclub.model.SubscriptionPeriod;
import com.karateclub.model.Member;
import com.karateclub.model.Payment;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionPeriodServiceImpl implements SubscriptionPeriodService {
    private SubscriptionPeriodDAO subscriptionPeriodDAO;

    public SubscriptionPeriodServiceImpl() {
        this.subscriptionPeriodDAO = new SubscriptionPeriodDAO();
    }

    public SubscriptionPeriodServiceImpl(SubscriptionPeriodDAO subscriptionPeriodDAO) {
        this.subscriptionPeriodDAO = subscriptionPeriodDAO;
    }

    @Override
    public SubscriptionPeriod getSubscriptionById(int periodId) {
        validatePeriodId(periodId);

        SubscriptionPeriod subscription = subscriptionPeriodDAO.getById(periodId);
        if (subscription == null) {
            throw new NotFoundException("Subscription period not found with ID: " + periodId);
        }
        return subscription;
    }

    @Override
    public List<SubscriptionPeriod> getAllSubscriptions() {
        return subscriptionPeriodDAO.getAll();
    }

    @Override
    public SubscriptionPeriod createSubscription(SubscriptionPeriod subscription) {
        validateSubscription(subscription);

        // Business rule: Check for overlapping subscriptions
        if (hasOverlappingSubscription(subscription.getMember().getMemberID(),
                subscription.getStartDate(), subscription.getEndDate())) {
            throw new BusinessRuleException("Member already has a subscription for this period");
        }

        // Business rule: End date must be after start date
        if (!subscription.getEndDate().isAfter(subscription.getStartDate())) {
            throw new ValidationException("End date must be after start date");
        }

        subscriptionPeriodDAO.save(subscription);
        return subscription;
    }

    @Override
    public SubscriptionPeriod updateSubscription(SubscriptionPeriod subscription) {
        validateSubscription(subscription);
        validateSubscriptionExists(subscription.getPeriodID());

        subscriptionPeriodDAO.update(subscription);
        return subscription;
    }

    @Override
    public void deleteSubscription(int periodId) {
        validatePeriodId(periodId);

        SubscriptionPeriod subscription = subscriptionPeriodDAO.getById(periodId);
        if (subscription != null) {
            // Business rule: Prevent deletion of paid subscriptions
            if (subscription.isPaid()) {
                throw new BusinessRuleException("Cannot delete paid subscription");
            }
            subscriptionPeriodDAO.delete(periodId);
        } else {
            throw new NotFoundException("Subscription period not found with ID: " + periodId);
        }
    }

    @Override
    public List<SubscriptionPeriod> getSubscriptionsByMember(int memberId) {
        validateMemberId(memberId);
        return subscriptionPeriodDAO.findByMember(memberId); // FIXED: findByMemberId → findByMember
    }

    @Override
    public SubscriptionPeriod getCurrentSubscription(int memberId) {
        validateMemberId(memberId);

        List<SubscriptionPeriod> subscriptions = getSubscriptionsByMember(memberId);
        LocalDateTime now = LocalDateTime.now();

        return subscriptions.stream()
                .filter(sub -> isSubscriptionActive(sub))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean hasActiveSubscription(int memberId) {
        validateMemberId(memberId);
        return subscriptionPeriodDAO.hasActiveSubscription(memberId); // FIXED: Using DAO method directly
    }

    @Override
    public SubscriptionPeriod renewSubscription(int memberId, LocalDateTime endDate) {
        validateMemberId(memberId);

        if (endDate == null || endDate.isBefore(LocalDateTime.now())) {
            throw new ValidationException("End date must be in the future");
        }

        SubscriptionPeriod currentSubscription = getCurrentSubscription(memberId);
        LocalDateTime startDate = currentSubscription != null ?
                currentSubscription.getEndDate() : LocalDateTime.now();

        // Business rule: New subscription must start after current one ends
        if (currentSubscription != null && startDate.isBefore(currentSubscription.getEndDate())) {
            startDate = currentSubscription.getEndDate();
        }

        SubscriptionPeriod newSubscription = new SubscriptionPeriod();
        newSubscription.setStartDate(startDate);
        newSubscription.setEndDate(endDate);
        newSubscription.setFees(calculateSubscriptionFee(memberId, endDate));
        newSubscription.setPaid(false);
        // Note: Member would need to be set by the caller

        return createSubscription(newSubscription);
    }

    @Override
    public boolean isSubscriptionActive(SubscriptionPeriod subscription) {
        if (subscription == null) return false;

        LocalDateTime now = LocalDateTime.now();
        return subscription.getStartDate().isBefore(now) &&
                subscription.getEndDate().isAfter(now) &&
                subscription.isPaid();
    }

    @Override
    public List<SubscriptionPeriod> getExpiringSubscriptions(int daysBeforeExpiry) {
        // Using your existing DAO method and filtering further
        return subscriptionPeriodDAO.findSubscriptionsExpiringSoon().stream()
                .filter(sub -> sub.getEndDate().isBefore(LocalDateTime.now().plusDays(daysBeforeExpiry)))
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionPeriod markAsPaid(int periodId, int paymentId) {
        validatePeriodId(periodId);

        SubscriptionPeriod subscription = getSubscriptionById(periodId);
        if (subscription.isPaid()) {
            throw new BusinessRuleException("Subscription is already paid");
        }

        subscription.setPaid(true);
        // Note: Payment entity would be set here in a real implementation

        subscriptionPeriodDAO.update(subscription);
        return subscription;
    }

    @Override
    public List<SubscriptionPeriod> getUnpaidSubscriptions(int memberId) {
        validateMemberId(memberId);
        // Using your existing DAO method and filtering by member
        return subscriptionPeriodDAO.findUnpaidSubscriptions().stream()
                .filter(sub -> sub.getMember().getMemberID() == memberId)
                .collect(Collectors.toList());
    }

    @Override
    public double calculateSubscriptionFee(int memberId, LocalDateTime endDate) {
        // Basic implementation - in reality, this would consider:
        // - Member's belt rank (different fees for different ranks)
        // - Duration of subscription
        // - Any discounts or promotions

        // For now, return a flat fee
        return 100.0; // Default monthly fee
    }

    // Validation methods
    private void validatePeriodId(int periodId) {
        if (periodId <= 0) {
            throw new ValidationException("Period ID must be positive");
        }
    }

    private void validateMemberId(int memberId) {
        if (memberId <= 0) {
            throw new ValidationException("Member ID must be positive");
        }
    }

    private void validateSubscription(SubscriptionPeriod subscription) {
        if (subscription == null) {
            throw new ValidationException("Subscription cannot be null");
        }
        if (subscription.getStartDate() == null) {
            throw new ValidationException("Start date is required");
        }
        if (subscription.getEndDate() == null) {
            throw new ValidationException("End date is required");
        }
        if (subscription.getFees() < 0) {
            throw new ValidationException("Fees cannot be negative");
        }
        if (subscription.getMember() == null) {
            throw new ValidationException("Member is required");
        }
    }

    private void validateSubscriptionExists(int periodId) {
        if (subscriptionPeriodDAO.getById(periodId) == null) {
            throw new NotFoundException("Subscription period not found with ID: " + periodId);
        }
    }

    private boolean hasOverlappingSubscription(int memberId, LocalDateTime startDate, LocalDateTime endDate) {
        List<SubscriptionPeriod> existingSubscriptions = getSubscriptionsByMember(memberId);

        return existingSubscriptions.stream()
                .anyMatch(existing ->
                        (startDate.isBefore(existing.getEndDate()) && endDate.isAfter(existing.getStartDate())));
    }
}