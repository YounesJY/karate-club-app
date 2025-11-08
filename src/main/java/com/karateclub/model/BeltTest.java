package com.karateclub.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "BeltTests")
public class BeltTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TestID")
    private int testID;

    @ManyToOne
    @JoinColumn(name = "MemberID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "RankID", nullable = false)
    private BeltRank rank;

    @Column(name = "Result", nullable = false)
    private boolean result;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "TestedByInstructorID", nullable = false)
    private Instructor testedByInstructor;

    @OneToOne
    @JoinColumn(name = "PaymentID")
    private Payment payment;

    // Constructors
    public BeltTest() {}

    public BeltTest(int testID, Member member, BeltRank rank, boolean result,
                    LocalDate date, Instructor testedByInstructor, Payment payment) {
        this.testID = testID;
        this.member = member;
        this.rank = rank;
        this.result = result;
        this.date = date;
        this.testedByInstructor = testedByInstructor;
        this.payment = payment;
    }

    // Getters and Setters
    public int getTestID() { return testID; }
    public void setTestID(int testID) { this.testID = testID; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public BeltRank getRank() { return rank; }
    public void setRank(BeltRank rank) { this.rank = rank; }

    public boolean isResult() { return result; }
    public void setResult(boolean result) { this.result = result; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Instructor getTestedByInstructor() { return testedByInstructor; }
    public void setTestedByInstructor(Instructor testedByInstructor) {
        this.testedByInstructor = testedByInstructor;
    }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    @Override
    public String toString() {
        return "BeltTest{" +
                "testID=" + testID +
                ", memberID=" + (member != null ? member.getMemberID() : "null") +
                ", rank=" + (rank != null ? rank.getRankName() : "null") +
                ", result=" + result +
                ", date=" + date +
                ", instructorID=" + (testedByInstructor != null ? testedByInstructor.getInstructorID() : "null") +
                ", paymentID=" + (payment != null ? payment.getPaymentID() : "null") +
                '}';
    }
}