package com.karateclub.service;

import com.karateclub.dao.InstructorDAO;
import com.karateclub.dao.MemberDAO;
import com.karateclub.dao.PersonDAO;
import com.karateclub.model.Instructor;
import com.karateclub.model.Member;
import com.karateclub.model.Person;
import com.karateclub.model.MemberInstructor;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class InstructorServiceImpl implements InstructorService {
    private InstructorDAO instructorDAO;
    private MemberDAO memberDAO;
    private PersonDAO personDAO;

    public InstructorServiceImpl() {
        this.instructorDAO = new InstructorDAO();
        this.memberDAO = new MemberDAO();
        this.personDAO = new PersonDAO();
    }

    public InstructorServiceImpl(InstructorDAO instructorDAO, MemberDAO memberDAO, PersonDAO personDAO) {
        this.instructorDAO = instructorDAO;
        this.memberDAO = memberDAO;
        this.personDAO = personDAO;
    }

    @Override
    public Instructor getInstructorById(int instructorId) {
        validateInstructorId(instructorId);

        Instructor instructor = instructorDAO.getById(instructorId);
        if (instructor == null) {
            throw new NotFoundException("Instructor not found with ID: " + instructorId);
        }
        return instructor;
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorDAO.getAll();
    }

    @Override
    public Instructor createInstructor(Instructor instructor) {
        validateInstructor(instructor);

        // Business rule: Person must exist and not already be an instructor
        if (instructor.getPerson() == null) {
            throw new ValidationException("Instructor must have an associated person");
        }

        Person person = personDAO.getById(instructor.getPerson().getPersonID());
        if (person == null) {
            throw new NotFoundException("Person not found with ID: " + instructor.getPerson().getPersonID());
        }

        // Check if person is already an instructor
        Instructor existingInstructor = instructorDAO.findByPersonId(person.getPersonID());
        if (existingInstructor != null) {
            throw new BusinessRuleException("Person is already an instructor");
        }

        instructorDAO.save(instructor);
        return instructor;
    }

    @Override
    public Instructor updateInstructor(Instructor instructor) {
        validateInstructor(instructor);
        validateInstructorExists(instructor.getInstructorID());

        instructorDAO.update(instructor);
        return instructor;
    }

    @Override
    public void deleteInstructor(int instructorId) {
        validateInstructorId(instructorId);

        Instructor instructor = instructorDAO.getById(instructorId);
        if (instructor != null) {
            // Business rule: Check if instructor has assigned members
            int studentCount = getInstructorStudentCount(instructorId);
            if (studentCount > 0) {
                throw new BusinessRuleException("Cannot delete instructor with assigned members");
            }

            instructorDAO.delete(instructorId);
        } else {
            throw new NotFoundException("Instructor not found with ID: " + instructorId);
        }
    }

    @Override
    public List<Instructor> getInstructorsByQualification(String qualification) {
        if (qualification == null || qualification.trim().isEmpty()) {
            throw new ValidationException("Qualification cannot be empty");
        }
        return instructorDAO.findByQualification(qualification);
    }

    @Override
    public List<Instructor> getActiveInstructors() {
        return instructorDAO.findActiveInstructors();
    }

    @Override
    public boolean isInstructorActive(int instructorId) {
        validateInstructorId(instructorId);
        return instructorDAO.isInstructorActive(instructorId);
    }

    @Override
    public Instructor assignMemberToInstructor(int instructorId, int memberId) {
        validateInstructorId(instructorId);
        validateMemberId(memberId);

        Instructor instructor = getInstructorById(instructorId);
        Member member = memberDAO.getById(memberId);

        if (member == null) {
            throw new NotFoundException("Member not found with ID: " + memberId);
        }

        // Check if assignment already exists
        boolean alreadyAssigned = instructor.getMemberInstructors().stream()
                .anyMatch(mi -> mi.getMember().getMemberID() == memberId);

        if (alreadyAssigned) {
            throw new BusinessRuleException("Member is already assigned to this instructor");
        }

        // Create new assignment
        MemberInstructor assignment = new MemberInstructor();
        assignment.setInstructor(instructor);
        assignment.setMember(member);
        assignment.setAssignDate(LocalDateTime.now());

        instructor.getMemberInstructors().add(assignment);
        instructorDAO.update(instructor);

        return instructor;
    }

    @Override
    public Instructor removeMemberFromInstructor(int instructorId, int memberId) {
        validateInstructorId(instructorId);
        validateMemberId(memberId);

        Instructor instructor = getInstructorById(instructorId);

        // Remove the assignment
        boolean removed = instructor.getMemberInstructors().removeIf(
                mi -> mi.getMember().getMemberID() == memberId
        );

        if (!removed) {
            throw new NotFoundException("Member is not assigned to this instructor");
        }

        instructorDAO.update(instructor);
        return instructor;
    }

    @Override
    public List<Member> getMembersByInstructor(int instructorId) {
        validateInstructorId(instructorId);

        Instructor instructor = getInstructorById(instructorId);
        return instructor.getMemberInstructors().stream()
                .map(MemberInstructor::getMember)
                .collect(Collectors.toList());
    }

    @Override
    public List<Instructor> getInstructorsByMember(int memberId) {
        validateMemberId(memberId);

        // This would be more efficient with a direct DAO method
        return getAllInstructors().stream()
                .filter(instructor -> instructor.getMemberInstructors().stream()
                        .anyMatch(mi -> mi.getMember().getMemberID() == memberId))
                .collect(Collectors.toList());
    }

    @Override
    public int getInstructorStudentCount(int instructorId) {
        validateInstructorId(instructorId);
        return instructorDAO.getStudentCount(instructorId);
    }

    @Override
    public boolean canInstructorTestRank(int instructorId, int rankId) {
        validateInstructorId(instructorId);
        validateRankId(rankId);

        // Basic implementation - all instructors can test all ranks
        // In reality, this would check instructor qualifications vs rank requirements
        return isInstructorActive(instructorId);
    }

    // Validation methods
    private void validateInstructorId(int instructorId) {
        if (instructorId <= 0) {
            throw new ValidationException("Instructor ID must be positive");
        }
    }

    private void validateMemberId(int memberId) {
        if (memberId <= 0) {
            throw new ValidationException("Member ID must be positive");
        }
    }

    private void validateRankId(int rankId) {
        if (rankId <= 0) {
            throw new ValidationException("Rank ID must be positive");
        }
    }

    private void validateInstructor(Instructor instructor) {
        if (instructor == null) {
            throw new ValidationException("Instructor cannot be null");
        }
        if (instructor.getPerson() == null) {
            throw new ValidationException("Instructor must have an associated person");
        }
        if (instructor.getQualification() == null || instructor.getQualification().trim().isEmpty()) {
            throw new ValidationException("Instructor qualification is required");
        }
    }

    private void validateInstructorExists(int instructorId) {
        if (instructorDAO.getById(instructorId) == null) {
            throw new NotFoundException("Instructor not found with ID: " + instructorId);
        }
    }
}