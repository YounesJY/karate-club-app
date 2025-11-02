package com.karateclub.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Member")
public class Member {
    @Id
    @Column(name = "MemberID")
    private int memberID;

    @OneToOne
    @JoinColumn(name = "PersonID", nullable = false)
    private Person person;

    @Column(name = "EmergencyContactInfo", length = 100, nullable = false)
    private String emergencyContactInfo;

    @ManyToOne
    @JoinColumn(name = "LastBeltRank", nullable = false)
    private BeltRank lastBeltRank;

    @Column(name = "IsActive", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<BeltTest> beltTests = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<SubscriptionPeriod> subscriptionPeriods = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "MemberInstructor",
            joinColumns = @JoinColumn(name = "MemberID"),
            inverseJoinColumns = @JoinColumn(name = "InstructorID")
    )
    private List<Instructor> instructors = new ArrayList<>();

    // Constructors
    public Member() {}

    public Member(int memberID, Person person, String emergencyContactInfo,
                  BeltRank lastBeltRank, boolean isActive) {
        this.memberID = memberID;
        this.person = person;
        this.emergencyContactInfo = emergencyContactInfo;
        this.lastBeltRank = lastBeltRank;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getMemberID() { return memberID; }
    public void setMemberID(int memberID) { this.memberID = memberID; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public String getEmergencyContactInfo() { return emergencyContactInfo; }
    public void setEmergencyContactInfo(String emergencyContactInfo) {
        this.emergencyContactInfo = emergencyContactInfo;
    }

    public BeltRank getLastBeltRank() { return lastBeltRank; }
    public void setLastBeltRank(BeltRank lastBeltRank) { this.lastBeltRank = lastBeltRank; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }

    public List<BeltTest> getBeltTests() { return beltTests; }
    public void setBeltTests(List<BeltTest> beltTests) { this.beltTests = beltTests; }

    public List<SubscriptionPeriod> getSubscriptionPeriods() { return subscriptionPeriods; }
    public void setSubscriptionPeriods(List<SubscriptionPeriod> subscriptionPeriods) {
        this.subscriptionPeriods = subscriptionPeriods;
    }

    public List<Instructor> getInstructors() { return instructors; }
    public void setInstructors(List<Instructor> instructors) { this.instructors = instructors; }

    @Override
    public String toString() {
        return "Member{" +
                "memberID=" + memberID +
                ", person=" + person +
                ", emergencyContactInfo='" + emergencyContactInfo + '\'' +
                ", lastBeltRank=" + lastBeltRank +
                ", isActive=" + isActive +
                '}';
    }
}