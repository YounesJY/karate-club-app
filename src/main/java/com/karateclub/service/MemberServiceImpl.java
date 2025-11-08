package com.karateclub.service;

import com.karateclub.dao.MemberDAO;
import com.karateclub.dao.BeltRankDAO;
import com.karateclub.dao.SubscriptionPeriodDAO;
import com.karateclub.model.Member;
import com.karateclub.model.BeltRank;


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

    @Override
    public Member getMemberById(int memberID) {
        // Business rule: Only return active members unless specifically requested
        Member member = memberDAO.getById(memberID);
        if (member != null && !member.isActive()) {
            throw new IllegalArgumentException("Member is inactive");
        }
        return member;
    }

    @Override
    public List<Member> getAllMembers() {
        return memberDAO.getAll();
    }

    @Override
    public Member createMember(Member member) {
        // Validation
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

        Member existing = memberDAO.getById(member.getMemberID());
        if (existing == null) {
            throw new IllegalArgumentException("Member not found: " + member.getMemberID());
        }

        memberDAO.update(member);
        return member;
    }

    @Override
    public void deleteMember(int memberID) {
        Member member = memberDAO.getById(memberID);
        if (member != null) {
            // Business rule: Don't actually delete, just deactivate
            member.setActive(false);
            memberDAO.update(member);
        }
    }

    @Override
    public List<Member> getActiveMembers() {
        return memberDAO.findActiveMembers();
    }

    @Override
    public List<Member> getMembersByBeltRank(int rankID) {
        return memberDAO.findByBeltRank(rankID);
    }

    @Override
    public Member promoteMember(int memberID, int newRankID) {
        Member member = memberDAO.getById(memberID);
        BeltRank newRank = beltRankDAO.getById(newRankID);

        if (member == null) throw new IllegalArgumentException("Member not found");
        if (newRank == null) throw new IllegalArgumentException("Invalid belt rank");
        if (!member.isActive()) throw new IllegalArgumentException("Inactive member cannot be promoted");

        // Business rule: Check if member is eligible for promotion
        if (!isMemberEligibleForPromotion(memberID)) {
            throw new IllegalArgumentException("Member is not eligible for promotion");
        }

        member.setLastBeltRank(newRank);
        memberDAO.update(member);

        return member;
    }

    @Override
    public boolean deactivateMember(int memberID) {
        Member member = memberDAO.getById(memberID);
        if (member != null && member.isActive()) {
            member.setActive(false);
            memberDAO.update(member);
            return true;
        }
        return false;
    }

    @Override
    public boolean activateMember(int memberID) {
        Member member = memberDAO.getById(memberID);
        if (member != null && !member.isActive()) {
            // Business rule: Check for unpaid fees before reactivation
            if (hasUnpaidFees(memberID)) {
                throw new IllegalArgumentException("Cannot activate member with unpaid fees");
            }
            member.setActive(true);
            memberDAO.update(member);
            return true;
        }
        return false;
    }

    @Override
    public boolean isMemberEligibleForPromotion(int memberID) {
        Member member = memberDAO.getById(memberID);
        if (member == null || !member.isActive()) return false;

        // Business rule: Check if member has active subscription
        if (!hasActiveSubscription(memberID)) return false;

        // Business rule: Check if member has unpaid fees
        if (hasUnpaidFees(memberID)) return false;

        // Additional business rules can be added here
        // (e.g., minimum time at current rank, passed required tests)

        return true;
    }

    @Override
    public boolean hasActiveSubscription(int memberID) {
        return subscriptionPeriodDAO.hasActiveSubscription(memberID);
    }

    @Override
    public boolean hasUnpaidFees(int memberID) {
        // This would check subscription fees, test fees, etc.
        // For now, let's assume we check if there are any unpaid subscriptions
        List<Member> membersWithUnpaid = memberDAO.findMembersWithUnpaidSubscriptions();
        return membersWithUnpaid.stream()
                .anyMatch(m -> m.getMemberID() == memberID);
    }

    // Private validation method
    private void validateMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        if (member.getPerson() == null) {
            throw new IllegalArgumentException("Member must have associated person");
        }
        if (member.getEmergencyContactInfo() == null ||
                member.getEmergencyContactInfo().trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact info is required");
        }
    }
}