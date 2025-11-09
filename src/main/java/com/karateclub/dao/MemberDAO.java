package com.karateclub.dao;

import com.karateclub.config.HibernateUtil;
import com.karateclub.model.Member;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class MemberDAO extends GenericDAO<Member> {

    public MemberDAO() {
        super(Member.class);
    }

    // BASIC METHODS (Lazy Loading - Production Default)
    // Get all members with basic details (both active and inactive)
    public List<Member> getAllMembersWithBasicDetails() {
        try (Session session = getSession()) {
            String hql = "SELECT DISTINCT m FROM Member m " +
                    "LEFT JOIN FETCH m.person " +
                    "LEFT JOIN FETCH m.lastBeltRank " +
                    "ORDER BY m.memberID";
            Query<Member> query = session.createQuery(hql, Member.class);
            return query.list();
        }
    }

    // Find active members (lazy loading)
    public List<Member> findActiveMembers() {
        return findByHQL("FROM Member m WHERE m.isActive = true");
    }

    // Find inactive members (lazy loading)
    public List<Member> findInactiveMembers() {
        return findByHQL("FROM Member m WHERE m.isActive = false");
    }

    // Find members by belt rank (lazy loading)
    public List<Member> findByBeltRank(int rankID) {
        try (Session session = getSession()) {
            String hql = "FROM Member m WHERE m.lastBeltRank.rankID = :rankID";
            Query<Member> query = session.createQuery(hql, Member.class);
            query.setParameter("rankID", rankID);
            return query.list();
        }
    }

    // Find members by name pattern (lazy loading)
    public List<Member> findByName(String namePattern) {
        return findByHQL("FROM Member m WHERE m.person.name LIKE ?1", "%" + namePattern + "%");
    }

    // ADVANCED METHODS (JOIN FETCH - Use Only When Needed)

    // FIXED: Get member with details for promotion (without multiple bag fetch)
    public Member getMemberWithDetails(int memberID) {
        try (Session session = getSession()) {
            String hql = "SELECT DISTINCT m FROM Member m " +
                    "LEFT JOIN FETCH m.person " +
                    "LEFT JOIN FETCH m.lastBeltRank " +
                    "WHERE m.memberID = :memberID";
            Query<Member> query = session.createQuery(hql, Member.class);
            query.setParameter("memberID", memberID);
            return query.uniqueResult();
        }
    }

    // Get all active members with basic details (JOIN FETCH - for lists)
    public List<Member> getActiveMembersWithBasicDetails() {
        try (Session session = getSession()) {
            String hql = "SELECT DISTINCT m FROM Member m " +
                    "LEFT JOIN FETCH m.person " +
                    "LEFT JOIN FETCH m.lastBeltRank " +
                    "WHERE m.isActive = true";
            Query<Member> query = session.createQuery(hql, Member.class);
            return query.list();
        }
    }

    // Get members with unpaid subscriptions (JOIN FETCH)
    public List<Member> findMembersWithUnpaidSubscriptions() {
        try (Session session = getSession()) {
            String hql = "SELECT DISTINCT m FROM Member m " +
                    "LEFT JOIN FETCH m.person " +
                    "JOIN m.subscriptionPeriods sp " +
                    "WHERE sp.paid = false";
            Query<Member> query = session.createQuery(hql, Member.class);
            return query.list();
        }
    }


    // BUSINESS LOGIC METHODS

    // In MemberDAO.java - Simplified for edit form
    public Member getMemberForEdit(int memberID) {
        try (Session session = getSession()) {
            String hql = "SELECT DISTINCT m FROM Member m " +
                    "LEFT JOIN FETCH m.person " +
                    "LEFT JOIN FETCH m.lastBeltRank " +
                    "WHERE m.memberID = :memberID";
            return session.createQuery(hql, Member.class)
                    .setParameter("memberID", memberID)
                    .uniqueResult();
        }
    }


    // In MemberDAO.java - Add this method
    public void updateMemberWithPerson(Member member) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // First update the Person entity
            if (member.getPerson() != null) {
                session.merge(member.getPerson()); // This ensures Person gets updated
            }

            // Then update the Member entity
            session.merge(member);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Get member's next belt rank
    public Integer getNextBeltRankID(int memberID) {
        try (Session session = getSession()) {
            Member member = session.get(Member.class, memberID);
            if (member != null && member.getLastBeltRank() != null) {
                int currentRank = member.getLastBeltRank().getRankID();
                return (currentRank < 7) ? currentRank + 1 : null; // 7 is black belt
            }
            return null;
        }
    }

    // Count members by activity status
    public long countActiveMembers() {
        try (Session session = getSession()) {
            String hql = "SELECT COUNT(*) FROM Member m WHERE m.isActive = true";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        }
    }

    public long countInactiveMembers() {
        try (Session session = getSession()) {
            String hql = "SELECT COUNT(*) FROM Member m WHERE m.isActive = false";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        }
    }


}