package com.karateclub.service;

import com.karateclub.dao.MemberDAO;
import com.karateclub.dao.BeltRankDAO;
import com.karateclub.dao.SubscriptionPeriodDAO;
import com.karateclub.model.Member;
import com.karateclub.model.BeltRank;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import java.util.List;

public class MemberServiceImpl implements MemberService {
    private MemberDAO memberDAO;
    private BeltRankDAO beltRankDAO;
    private SubscriptionPeriodDAO subscriptionPeriodDAO;

    public MemberServiceImpl() {
        this.memberDAO = new MemberDAO();
        this.beltRankDAO = new BeltRankDAO();
        this.subscriptionPeriodDAO = new SubscriptionPeriodDAO();
    }

    // BASIC CRUD OPERATIONS

    @Override
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembersWithBasicDetails();
    }

    @Override
    public Member getMemberById(int id) {
        // Use simplified fetch for edit forms
        Member member = memberDAO.getMemberForEdit(id);
        if (member == null) {
            throw new NotFoundException("Member not found with ID: " + id);
        }
        return member;
    }


    @Override
    public List<Member> getActiveMembers() {
        // Use the optimized method from DAO
        return memberDAO.getActiveMembersWithBasicDetails();
    }

    @Override
    public Member createMember(Member member) {
        validateMember(member);

        // Business rule: New members start as active
        member.setActive(true);

        // Always resolve BeltRank to a managed entity (use provided ID or default to white belt = 1)
        int desiredRankId = 1; // default white belt
        if (member.getLastBeltRank() != null && member.getLastBeltRank().getRankID() > 0) {
            desiredRankId = member.getLastBeltRank().getRankID();
        }
        BeltRank managedRank = beltRankDAO.getById(desiredRankId);
        if (managedRank == null) {
            throw new NotFoundException("Belt rank not found with ID: " + desiredRankId);
        }
        member.setLastBeltRank(managedRank);

        memberDAO.save(member);
        return member;
    }

    @Override
    public Member updateMember(Member member) {
        validateMember(member);
        validateMemberExists(member.getMemberID());

        // Use the new method that handles Person updates
        memberDAO.updateMemberWithPerson(member);
        return member;
    }

    @Override
    public void deleteMember(int memberID) {
        validateMemberID(memberID);

        Member member = memberDAO.getById(memberID);
        if (member == null) {
            throw new NotFoundException("Member not found with ID: " + memberID);
        }

        // Use the inherited delete method from GenericDAO
        memberDAO.delete(memberID);
    }

    // QUERY OPERATIONS

    @Override
    public List<Member> getMembersByBeltRank(int rankID) {
        validateBeltRankID(rankID);
        return memberDAO.findByBeltRank(rankID);
    }

    public List<Member> searchMembersByName(String namePattern) {
        if (namePattern == null || namePattern.trim().isEmpty()) {
            return memberDAO.getActiveMembersWithBasicDetails();
        }
        return memberDAO.findByName(namePattern.trim());
    }

    // MEMBER MANAGEMENT OPERATIONS

    @Override
    public Member promoteMember(int memberID, int newRankID) {
        validateMemberID(memberID);
        validateBeltRankID(newRankID);

        Member member = memberDAO.getMemberWithDetails(memberID);
        BeltRank newRank = beltRankDAO.getById(newRankID);

        if (member == null) throw new NotFoundException("Member not found with ID: " + memberID);
        if (newRank == null) throw new NotFoundException("Belt rank not found with ID: " + newRankID);
        if (!member.isActive()) throw new BusinessRuleException("Inactive member cannot be promoted");

        // Business rule: Check if member is eligible for promotion
        if (!isMemberEligibleForPromotion(memberID)) {
            throw new BusinessRuleException("Member is not eligible for promotion");
        }

        member.setLastBeltRank(newRank);
        memberDAO.update(member);

        return member;
    }

    @Override
    public boolean deactivateMember(int memberID) {
        validateMemberID(memberID);

        Member member = memberDAO.getById(memberID);
        if (member == null) {
            throw new NotFoundException("Member not found with ID: " + memberID);
        }

        if (member.isActive()) {
            member.setActive(false);
            memberDAO.update(member);
            return true;
        }
        return false;
    }

    @Override
    public boolean activateMember(int memberID) {
        validateMemberID(memberID);

        Member member = memberDAO.getById(memberID);
        if (member == null) {
            throw new NotFoundException("Member not found with ID: " + memberID);
        }

        if (!member.isActive()) {
            // Business rule: Check for unpaid fees before reactivation
            if (hasUnpaidFees(memberID)) {
                throw new BusinessRuleException("Cannot activate member with unpaid fees");
            }
            member.setActive(true);
            memberDAO.update(member);
            return true;
        }
        return false;
    }

    // BUSINESS RULE CHECKS

    @Override
    public boolean isMemberEligibleForPromotion(int memberID) {
        validateMemberID(memberID);

        Member member = memberDAO.getById(memberID);
        if (member == null || !member.isActive()) {
            return false;
        }

        // More specific checks with better messaging potential
        if (!hasActiveSubscription(memberID)) {
            throw new BusinessRuleException("Member has no active subscription. Please add a subscription first.");
        }

        if (hasUnpaidFees(memberID)) {
            throw new BusinessRuleException("Member has unpaid fees. Please process payment before promotion.");
        }

        return true;
    }

    @Override
    public boolean hasActiveSubscription(int memberID) {
        return subscriptionPeriodDAO.hasActiveSubscription(memberID);
    }

    @Override
    public boolean hasUnpaidFees(int memberID) {
        // Use the optimized DAO method instead of loading all members
        List<Member> membersWithUnpaid = memberDAO.findMembersWithUnpaidSubscriptions();
        return membersWithUnpaid.stream()
                .anyMatch(m -> m.getMemberID() == memberID);
    }

    // UTILITY METHODS

    public Integer getNextBeltRank(int memberID) {
        validateMemberID(memberID);
        return memberDAO.getNextBeltRankID(memberID);
    }

    public long getActiveMembersCount() {
        return memberDAO.countActiveMembers();
    }

    public long getInactiveMembersCount() {
        return memberDAO.countInactiveMembers();
    }

    // VALIDATION METHODS

    private void validateMemberID(int memberID) {
        if (memberID <= 0) {
            throw new ValidationException("Member ID must be positive");
        }
    }

    private void validateBeltRankID(int rankID) {
        if (rankID <= 0) {
            throw new ValidationException("Belt rank ID must be positive");
        }
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new ValidationException("Member cannot be null");
        }
        if (member.getPerson() == null) {
            throw new ValidationException("Member must have associated person");
        }
        if (member.getPerson().getName() == null || member.getPerson().getName().trim().isEmpty()) {
            throw new ValidationException("Member name is required");
        }

        // Contact Info Validation
        String contactInfo = member.getPerson().getContactInfo();
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            throw new ValidationException("Contact information is required");
        }
        if (contactInfo.trim().length() < 5) {
            throw new ValidationException("Contact info must be at least 5 characters");
        }
        if (!isValidContactInfo(contactInfo.trim())) {
            throw new ValidationException("Contact info must be a valid phone number or email");
        }

        // Emergency Contact Validation
        String emergencyContact = member.getEmergencyContactInfo();
        if (emergencyContact == null || emergencyContact.trim().isEmpty()) {
            throw new ValidationException("Emergency contact info is required");
        }
        if (emergencyContact.trim().length() < 5) {
            throw new ValidationException("Emergency contact info must be at least 5 characters");
        }
        if (!isValidContactInfo(emergencyContact.trim())) {
            throw new ValidationException("Emergency contact must be a valid phone number");
        }
    }

    // Helper method for contact validation
    private boolean isValidContactInfo(String contact) {
        // Phone number pattern: allows numbers, spaces, hyphens, parentheses
        String phonePattern = "^[\\d\\s\\-\\(\\)\\+]+$";

        // Email pattern
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";

        return contact.matches(phonePattern) || contact.matches(emailPattern);
    }

    private void validateMemberExists(int memberID) {
        if (memberDAO.getById(memberID) == null) {
            throw new NotFoundException("Member not found with ID: " + memberID);
        }
    }
}

