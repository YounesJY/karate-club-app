package com.karateclub.dao;

import com.karateclub.model.Payment;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PaymentDAO extends GenericDAO<Payment> {

    public PaymentDAO() {
        super(Payment.class);
    }

    // Find payments by member
    public List<Payment> findByMember(int memberID) {
        return findByHQL("FROM Payment p WHERE p.member.memberID = ?1", memberID);
    }

    // Find payments by date range
    public List<Payment> findByDateRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = getSession()) {
            String hql = "FROM Payment p WHERE p.date BETWEEN :startDate AND :endDate";
            Query<Payment> query = session.createQuery(hql, Payment.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        }
    }

    // Find payments above certain amount
    public List<Payment> findPaymentsAboveAmount(double amount) {
        return findByHQL("FROM Payment p WHERE p.amount > ?1", amount);
    }

    // Get total payments for a member
    public double getTotalPaymentsByMember(int memberID) {
        try (Session session = getSession()) {
            String hql = "SELECT SUM(p.amount) FROM Payment p WHERE p.member.memberID = :memberID";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("memberID", memberID);
            Double result = query.uniqueResult();
            return result != null ? result : 0.0;
        }
    }
}