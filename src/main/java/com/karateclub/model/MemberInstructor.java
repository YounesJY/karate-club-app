package com.karateclub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MemberInstructor")
public class MemberInstructor {

    @EmbeddedId
    private MemberInstructorId id;

    @ManyToOne
    @MapsId("memberID")
    @JoinColumn(name = "MemberID")
    private Member member;

    @ManyToOne
    @MapsId("instructorID")
    @JoinColumn(name = "InstructorID")
    private Instructor instructor;

    @Column(name = "AssignDate", nullable = false)
    private LocalDateTime assignDate;

    // Constructors
    public MemberInstructor() {}

    public MemberInstructor(Member member, Instructor instructor, LocalDateTime assignDate) {
        this.member = member;
        this.instructor = instructor;
        this.assignDate = assignDate;
        this.id = new MemberInstructorId(member.getMemberID(), instructor.getInstructorID());
    }

    // Getters and Setters
    public MemberInstructorId getId() { return id; }
    public void setId(MemberInstructorId id) { this.id = id; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }

    public LocalDateTime getAssignDate() { return assignDate; }
    public void setAssignDate(LocalDateTime assignDate) { this.assignDate = assignDate; }
}

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