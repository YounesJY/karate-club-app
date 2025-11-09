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
        // Use detailed fetch for individual member views
        Member member = memberDAO.getMemberWithDetails(id);
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

        // Business rule: Set default belt rank if not provided
        if (member.getLastBeltRank() == null) {
            BeltRank whiteBelt = beltRankDAO.getById(1); // White belt
            member.setLastBeltRank(whiteBelt);
        }

        memberDAO.save(member);
        return member;
    }

    @Override
    public Member updateMember(Member member) {
        validateMember(member);
        validateMemberExists(member.getMemberID());

        memberDAO.update(member);
        return member;
    }

    @Override
    public void deleteMember(int memberID) {
        validateMemberID(memberID);

        Member member = memberDAO.getById(memberID);
        if (member != null) {
            // Business rule: Don't actually delete, just deactivate
            member.setActive(false);
            memberDAO.update(member);
        } else {
            throw new NotFoundException("Member not found with ID: " + memberID);
        }
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

        // Business rule: Check if member has active subscription
        if (!hasActiveSubscription(memberID)) {
            return false;
        }

        // Business rule: Check if member has unpaid fees
        if (hasUnpaidFees(memberID)) {
            return false;
        }

        // Additional business rules can be added here
        // Example: Check minimum time at current rank, attendance requirements, etc.

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
        if (member.getPerson().getContactInfo() == null || member.getPerson().getContactInfo().trim().isEmpty()) {
            throw new ValidationException("Contact information is required");
        }
        if (member.getEmergencyContactInfo() == null || member.getEmergencyContactInfo().trim().isEmpty()) {
            throw new ValidationException("Emergency contact info is required");
        }
        if (member.getEmergencyContactInfo().trim().length() < 5) {
            throw new ValidationException("Emergency contact info must be at least 5 characters");
        }
    }

    private void validateMemberExists(int memberID) {
        if (memberDAO.getById(memberID) == null) {
            throw new NotFoundException("Member not found with ID: " + memberID);
        }
    }
}