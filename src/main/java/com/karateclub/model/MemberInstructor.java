package com.karateclub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MemberInstructors")
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
