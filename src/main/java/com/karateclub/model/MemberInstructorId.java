package com.karateclub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Embeddable
class MemberInstructorId implements java.io.Serializable {

    @Column(name = "MemberID")
    private int memberID;

    @Column(name = "InstructorID")
    private int instructorID;

    // Constructors
    public MemberInstructorId() {}

    public MemberInstructorId(int memberID, int instructorID) {
        this.memberID = memberID;
        this.instructorID = instructorID;
    }

    // Getters and Setters
    public int getMemberID() { return memberID; }
    public void setMemberID(int memberID) { this.memberID = memberID; }

    public int getInstructorID() { return instructorID; }
    public void setInstructorID(int instructorID) { this.instructorID = instructorID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberInstructorId that = (MemberInstructorId) o;
        return memberID == that.memberID && instructorID == that.instructorID;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(memberID, instructorID);
    }
}