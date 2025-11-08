package com.karateclub.dao;

import com.karateclub.model.MemberInstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MemberInstructorDAO extends GenericDAO<MemberInstructor> {

    public MemberInstructorDAO() {
        super(MemberInstructor.class);
    }

    // Find assignments by member
    public List<MemberInstructor> findByMember(int memberID) {
        return findByHQL("FROM MemberInstructor mi WHERE mi.member.memberID = ?1", memberID);
    }

    // Find assignments by instructor
    public List<MemberInstructor> findByInstructor(int instructorID) {
        return findByHQL("FROM MemberInstructor mi WHERE mi.instructor.instructorID = ?1", instructorID);
    }

    // Check if assignment exists
    public boolean assignmentExists(int memberID, int instructorID) {
        try (Session session = getSession()) {
            String hql = "SELECT COUNT(mi) FROM MemberInstructor mi " +
                    "WHERE mi.member.memberID = :memberID AND mi.instructor.instructorID = :instructorID";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("memberID", memberID);
            query.setParameter("instructorID", instructorID);
            return query.uniqueResult() > 0;
        }
    }

    // Remove assignment
    public boolean removeAssignment(int memberID, int instructorID) {
        try (Session session = getSession()) {
            String hql = "DELETE FROM MemberInstructor mi " +
                    "WHERE mi.member.memberID = :memberID AND mi.instructor.instructorID = :instructorID";
            Query<?> query = session.createQuery(hql);
            query.setParameter("memberID", memberID);
            query.setParameter("instructorID", instructorID);
            int deleted = query.executeUpdate();
            return deleted > 0;
        }
    }
}