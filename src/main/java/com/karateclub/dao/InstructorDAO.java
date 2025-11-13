package com.karateclub.dao;

import com.karateclub.model.Instructor;
import com.karateclub.model.Member;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class InstructorDAO extends GenericDAO<Instructor> {

    public InstructorDAO() {
        super(Instructor.class);
    }

    // Find instructors by qualification
    public List<Instructor> findByQualification(String qualification) {
        return findByHQL("FROM Instructor i WHERE i.qualification LIKE ?1", "%" + qualification + "%");
    }

    // Find instructors by person (useful for authentication later)
    public Instructor findByPersonId(int personId) {
        List<Instructor> results = findByHQL("FROM Instructor i WHERE i.person.personID = ?1", personId);
        return results.isEmpty() ? null : results.get(0);
    }

    // Check if instructor exists and is active (has associated person)
    public boolean isInstructorActive(int instructorId) {
        List<Instructor> results = findByHQL(
                "FROM Instructor i WHERE i.instructorID = ?1 AND i.person IS NOT NULL",
                instructorId
        );
        return !results.isEmpty();
    }

    public List<Instructor> findActiveInstructors() {
        return findByHQL("FROM Instructor i WHERE i.person IS NOT NULL");
    }

    // Get instructor student count - simplified version
    public int getStudentCount(int instructorId) {
        Instructor instructor = getById(instructorId);
        if (instructor != null && instructor.getMemberInstructors() != null) {
            return instructor.getMemberInstructors().size();
        }
        return 0;
    }

    // Add to InstructorDAO.java
    public List<Member> getStudentsByInstructor(int instructorId) {
        try (Session session = getSession()) {
            String hql = "SELECT mi.member FROM MemberInstructor mi WHERE mi.instructor.instructorID = :instructorId";
            Query<Member> query = session.createQuery(hql, Member.class);
            query.setParameter("instructorId", instructorId);
            return query.list();
        }
    }

}