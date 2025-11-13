package com.karateclub.dao;

import com.karateclub.model.BeltRank;
import com.karateclub.model.BeltTest;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class BeltTestDAO extends GenericDAO<BeltTest> {

    public BeltTestDAO() {
        super(BeltTest.class);
    }

    // Find tests by member
    public List<BeltTest> findByMember(int memberID) {
        return findByHQL("FROM BeltTest bt WHERE bt.member.memberID = ?1", memberID);
    }

    // Find tests by instructor
    public List<BeltTest> findByInstructor(int instructorID) {
        return findByHQL("FROM BeltTest bt WHERE bt.testedByInstructor.instructorID = ?1", instructorID);
    }

    // Find tests by result (pass/fail)
    public List<BeltTest> findByResult(boolean result) {
        return findByHQL("FROM BeltTest bt WHERE bt.result = ?1", result);
    }

    // Find tests by date range
    public List<BeltTest> findByDateRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = getSession()) {
            String hql = "FROM BeltTest bt WHERE bt.date BETWEEN :startDate AND :endDate";
            Query<BeltTest> query = session.createQuery(hql, BeltTest.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        }
    }

    // Get passed tests count for a member
    public long getPassedTestsCount(int memberID) {
        try (Session session = getSession()) {
            String hql = "SELECT COUNT(bt) FROM BeltTest bt WHERE bt.member.memberID = :memberID AND bt.result = true";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("memberID", memberID);
            return query.uniqueResult();
        }
    }

    public List<BeltTest> getPendingTests() {
        return findByHQL("FROM BeltTest bt WHERE bt.result IS NULL");
    }

    public List<BeltTest> getUpcomingTestsByInstructor(int instructorId) {
        try (Session session = getSession()) {
            String hql = "FROM BeltTest bt WHERE bt.testedByInstructor.instructorID = :instructorId AND bt.result IS NULL";
            Query<BeltTest> query = session.createQuery(hql, BeltTest.class);
            query.setParameter("instructorId", instructorId);
            return query.list();
        }
    }
    
}