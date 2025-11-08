package com.karateclub.service;

import com.karateclub.model.SubscriptionPeriod;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionPeriodService {

    // Basic CRUD operations
    SubscriptionPeriod getSubscriptionById(int periodId);
    List<SubscriptionPeriod> getAllSubscriptions();
    SubscriptionPeriod createSubscription(SubscriptionPeriod subscription);
    SubscriptionPeriod updateSubscription(SubscriptionPeriod subscription);
    void deleteSubscription(int periodId);

    // Member-specific operations
    List<SubscriptionPeriod> getSubscriptionsByMember(int memberId);
    SubscriptionPeriod getCurrentSubscription(int memberId);
    boolean hasActiveSubscription(int memberId);

    // Business operations
    SubscriptionPeriod renewSubscription(int memberId, LocalDateTime endDate);
    boolean isSubscriptionActive(SubscriptionPeriod subscription);
    List<SubscriptionPeriod> getExpiringSubscriptions(int daysBeforeExpiry);

    // Payment-related operations
    SubscriptionPeriod markAsPaid(int periodId, int paymentId);
    List<SubscriptionPeriod> getUnpaidSubscriptions(int memberId);

    // Utility methods
    double calculateSubscriptionFee(int memberId, LocalDateTime endDate);
}