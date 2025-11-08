package com.karateclub.service;

import com.karateclub.model.Instructor;
import com.karateclub.model.Member;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.util.List;

public interface InstructorService {

    // Basic CRUD operations
    Instructor getInstructorById(int instructorId);
    List<Instructor> getAllInstructors();
    Instructor createInstructor(Instructor instructor);
    Instructor updateInstructor(Instructor instructor);
    void deleteInstructor(int instructorId);

    // Business operations
    List<Instructor> getInstructorsByQualification(String qualification);
    List<Instructor> getActiveInstructors();
    boolean isInstructorActive(int instructorId);

    // Member assignment operations (for the many-to-many relationship)
    Instructor assignMemberToInstructor(int instructorId, int memberId);
    Instructor removeMemberFromInstructor(int instructorId, int memberId);
    List<Member> getMembersByInstructor(int instructorId);
    List<Instructor> getInstructorsByMember(int memberId);

    // Utility methods
    int getInstructorStudentCount(int instructorId);
    boolean canInstructorTestRank(int instructorId, int rankId);
}