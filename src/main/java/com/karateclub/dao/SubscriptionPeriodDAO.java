package com.karateclub.dao;

import com.karateclub.model.SubscriptionPeriod;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.MutationQuery;

import java.time.LocalDateTime;
import java.util.List;

public class SubscriptionPeriodDAO extends GenericDAO<SubscriptionPeriod> {

    public SubscriptionPeriodDAO() {
        super(SubscriptionPeriod.class);
    }

    // Find subscriptions by member
    public List<SubscriptionPeriod> findByMember(int memberID) {
        return findByHQL("FROM SubscriptionPeriod sp WHERE sp.member.memberID = ?1", memberID);
    }

    // Find active subscriptions (current date within period)
    public List<SubscriptionPeriod> findActiveSubscriptions() {
        try (Session session = getSession()) {
            String hql = "FROM SubscriptionPeriod sp WHERE sp.startDate <= CURRENT_TIMESTAMP AND sp.endDate >= CURRENT_TIMESTAMP";
            Query<SubscriptionPeriod> query = session.createQuery(hql, SubscriptionPeriod.class);
            return query.list();
        }
    }

    // Find unpaid subscriptions
    public List<SubscriptionPeriod> findUnpaidSubscriptions() {
        return findByHQL("FROM SubscriptionPeriod sp WHERE sp.paid = false");
    }

    // Find subscriptions expiring soon (within next 30 days)
    public List<SubscriptionPeriod> findSubscriptionsExpiringSoon() {
        try (Session session = getSession()) {
            String hql = "FROM SubscriptionPeriod sp WHERE sp.endDate BETWEEN CURRENT_TIMESTAMP AND :futureDate";
            Query<SubscriptionPeriod> query = session.createQuery(hql, SubscriptionPeriod.class);
            query.setParameter("futureDate", LocalDateTime.now().plusDays(30));
            return query.list();
        }
    }

    // Check if member has active subscription
    public boolean hasActiveSubscription(int memberID) {
        try (Session session = getSession()) {
            String hql = "SELECT COUNT(sp) FROM SubscriptionPeriod sp WHERE sp.member.memberID = :memberID " +
                    "AND sp.startDate <= CURRENT_TIMESTAMP AND sp.endDate >= CURRENT_TIMESTAMP " +
                    "AND sp.paid = true";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("memberID", memberID);
            return query.uniqueResult() > 0;
        }
    }

    // Get current active subscription for a member
    public SubscriptionPeriod getCurrentSubscription(int memberID) {
        try (Session session = getSession()) {
            String hql = "FROM SubscriptionPeriod sp WHERE sp.member.memberID = :memberID " +
                    "AND sp.startDate <= CURRENT_TIMESTAMP AND sp.endDate >= CURRENT_TIMESTAMP " +
                    "AND sp.paid = true ORDER BY sp.endDate DESC";
            Query<SubscriptionPeriod> query = session.createQuery(hql, SubscriptionPeriod.class);
            query.setParameter("memberID", memberID);
            query.setMaxResults(1);
            List<SubscriptionPeriod> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        }
    }

    // Get latest subscription for a member (active or not)
    public SubscriptionPeriod getLatestSubscription(int memberID) {
        try (Session session = getSession()) {
            String hql = "FROM SubscriptionPeriod sp WHERE sp.member.memberID = :memberID " +
                    "ORDER BY sp.endDate DESC";
            Query<SubscriptionPeriod> query = session.createQuery(hql, SubscriptionPeriod.class);
            query.setParameter("memberID", memberID);
            query.setMaxResults(1);
            List<SubscriptionPeriod> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        }
    }

    // Find subscription periods by payment ID
    public List<SubscriptionPeriod> findByPayment(int paymentID) {
        return findByHQL("FROM SubscriptionPeriod sp WHERE sp.payment.paymentID = ?1", paymentID);
    }

    // Nullify payment reference for subscription periods
    public void nullifyPaymentReference(int paymentID) {
        try (Session session = getSession()) {
            session.beginTransaction();
            String hql = "UPDATE SubscriptionPeriod sp SET sp.payment = null WHERE sp.payment.paymentID = :paymentID";
            MutationQuery query = session.createMutationQuery(hql);
            query.setParameter("paymentID", paymentID);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }
}