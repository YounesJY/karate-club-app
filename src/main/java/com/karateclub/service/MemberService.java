package com.karateclub.service;

import com.karateclub.model.Member;
import java.util.List;

public interface MemberService {

    // Basic CRUD operations
    Member getMemberById(int memberID);
    List<Member> getAllMembers();
    Member createMember(Member member);
    Member updateMember(Member member);
    void deleteMember(int memberID);

    // Business operations
    List<Member> getActiveMembers();
    List<Member> getMembersByBeltRank(int rankID);
    Member promoteMember(int memberID, int newRankID);
    boolean deactivateMember(int memberID);
    boolean activateMember(int memberID);

    // Validation and business rules
    boolean isMemberEligibleForPromotion(int memberID);
    boolean hasActiveSubscription(int memberID);
    boolean hasUnpaidFees(int memberID);
}