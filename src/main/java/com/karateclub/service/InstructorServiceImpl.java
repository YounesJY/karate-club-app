package com.karateclub.service;

import com.karateclub.dao.InstructorDAO;
import com.karateclub.dao.MemberDAO;
import com.karateclub.dao.PersonDAO;
import com.karateclub.dao.MemberInstructorDAO;
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
    private MemberInstructorDAO memberInstructorDAO;

    public InstructorServiceImpl() {
        this.instructorDAO = new InstructorDAO();
        this.memberDAO = new MemberDAO();
        this.personDAO = new PersonDAO();
        this.memberInstructorDAO = new MemberInstructorDAO();
    }

    public InstructorServiceImpl(InstructorDAO instructorDAO, MemberDAO memberDAO, PersonDAO personDAO) {
        this.instructorDAO = instructorDAO;
        this.memberDAO = memberDAO;
        this.personDAO = personDAO;
        this.memberInstructorDAO = new MemberInstructorDAO();
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
        validateInstructorWithPerson(instructor);
        // If person has no ID (0), persist via personDAO; else merge existing
        Person p = instructor.getPerson();
        if (p.getPersonID() == 0) {
            personDAO.save(p);
        } else {
            Person existing = personDAO.getById(p.getPersonID());
            if (existing == null) {
                // treat as new if ID provided but not found
                p.setPersonID(0);
                personDAO.save(p);
            } else {
                // update fields then merge
                existing.setName(p.getName());
                existing.setAddress(p.getAddress());
                existing.setContactInfo(p.getContactInfo());
                personDAO.update(existing);
                instructor.setPerson(existing);
            }
        }
        instructorDAO.save(instructor);
        return instructor;
    }

    @Override
    public Instructor updateInstructor(Instructor instructor) {
        validateInstructorWithPerson(instructor);
        validateInstructorExists(instructor.getInstructorID());
        // Merge person changes
        Person p = instructor.getPerson();
        if (p != null) {
            if (p.getPersonID() == 0) {
                personDAO.save(p);
            } else {
                Person managed = personDAO.getById(p.getPersonID());
                if (managed != null) {
                    managed.setName(p.getName());
                    managed.setAddress(p.getAddress());
                    managed.setContactInfo(p.getContactInfo());
                    personDAO.update(managed);
                    instructor.setPerson(managed);
                } else {
                    personDAO.save(p);
                }
            }
        }
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

        // Validate both sides exist
        if (instructorDAO.getById(instructorId) == null) {
            throw new NotFoundException("Instructor not found with ID: " + instructorId);
        }
        if (memberDAO.getById(memberId) == null) {
            throw new NotFoundException("Member not found with ID: " + memberId);
        }

        // Check if assignment already exists via DAO
        if (memberInstructorDAO.assignmentExists(memberId, instructorId)) {
            throw new BusinessRuleException("Member is already assigned to this instructor");
        }

        // Persist assignment
        memberInstructorDAO.createAssignment(instructorId, memberId, LocalDateTime.now());
        return getInstructorById(instructorId);
    }

    @Override
    public Instructor removeMemberFromInstructor(int instructorId, int memberId) {
        validateInstructorId(instructorId);
        validateMemberId(memberId);

        // Validate exists
        if (instructorDAO.getById(instructorId) == null) {
            throw new NotFoundException("Instructor not found with ID: " + instructorId);
        }

        // Remove via DAO
        boolean removed = memberInstructorDAO.removeAssignment(memberId, instructorId);
        if (!removed) {
            throw new NotFoundException("Member is not assigned to this instructor");
        }
        return getInstructorById(instructorId);
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

    private void validateInstructorWithPerson(Instructor instructor) {
        if (instructor == null) {
            throw new ValidationException("Instructor cannot be null");
        }
        if (instructor.getPerson() == null) {
            throw new ValidationException("Instructor must include person details");
        }
        Person p = instructor.getPerson();
        if (p.getName() == null || p.getName().trim().isEmpty()) {
            throw new ValidationException("Person name is required");
        }
        if (p.getContactInfo() == null || p.getContactInfo().trim().isEmpty()) {
            throw new ValidationException("Contact info is required");
        }
        if (instructor.getQualification() == null || instructor.getQualification().trim().isEmpty()) {
            throw new ValidationException("Qualification is required");
        }
    }

    private void validateInstructorExists(int instructorId) {
        if (instructorDAO.getById(instructorId) == null) {
            throw new NotFoundException("Instructor not found with ID: " + instructorId);
        }
    }
}