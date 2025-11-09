package com.karateclub.dao;

import com.karateclub.config.HibernateUtil;
import com.karateclub.model.MemberInstructor;
import com.karateclub.model.Member;
import com.karateclub.model.Instructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
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

    // Remove assignment (must be in a transaction)
    public boolean removeAssignment(int memberID, int instructorID) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            String hql = "DELETE FROM MemberInstructor mi " +
                    "WHERE mi.member.memberID = :memberID AND mi.instructor.instructorID = :instructorID";
            Query<?> query = session.createQuery(hql);
            query.setParameter("memberID", memberID);
            query.setParameter("instructorID", instructorID);
            int deleted = query.executeUpdate();
            tx.commit();
            return deleted > 0;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // Create assignment using managed references to avoid transient/detached issues
    public void createAssignment(int instructorID, int memberID, LocalDateTime assignDate) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Member member = session.get(Member.class, memberID);
            Instructor instructor = session.get(Instructor.class, instructorID);
            if (member == null || instructor == null) {
                throw new IllegalArgumentException("Member or Instructor not found for assignment");
            }
            MemberInstructor assignment = new MemberInstructor(member, instructor, assignDate);
            session.persist(assignment);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }
}