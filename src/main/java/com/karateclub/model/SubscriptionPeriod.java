package com.karateclub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SubscriptionPeriods")
public class SubscriptionPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PeriodID")
    private int periodID;

    @Column(name = "StartDate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "Fees", nullable = false)
    private double fees;

    @Column(name = "Paid", nullable = false)
    private boolean paid;

    @ManyToOne
    @JoinColumn(name = "MemberID", nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "PaymentID")
    private Payment payment;

    // Constructors
    public SubscriptionPeriod() {}

    public SubscriptionPeriod(int periodID, LocalDateTime startDate, LocalDateTime endDate,
                              double fees, boolean paid, Member member, Payment payment) {
        this.periodID = periodID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fees = fees;
        this.paid = paid;
        this.member = member;
        this.payment = payment;
    }

    // Getters and Setters
    public int getPeriodID() { return periodID; }
    public void setPeriodID(int periodID) { this.periodID = periodID; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public double getFees() { return fees; }
    public void setFees(double fees) { this.fees = fees; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    @Override
    public String toString() {
        return "SubscriptionPeriod{" +
                "periodID=" + periodID +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", fees=" + fees +
                ", paid=" + paid +
                ", memberID=" + (member != null ? member.getMemberID() : "null") +
                ", paymentID=" + (payment != null ? payment.getPaymentID() : "null") +
                '}';
    }
}