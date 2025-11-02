package com.karateclub.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Payment")
public class Payment {
    @Id
    @Column(name = "PaymentID")
    private int paymentID;

    @Column(name = "Amount", nullable = false)
    private double amount;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "MemberID", nullable = false)
    private Member member;

    // Constructors
    public Payment() {}

    public Payment(int paymentID, double amount, LocalDate date, Member member) {
        this.paymentID = paymentID;
        this.amount = amount;
        this.date = date;
        this.member = member;
    }

    // Getters and Setters
    public int getPaymentID() { return paymentID; }
    public void setPaymentID(int paymentID) { this.paymentID = paymentID; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", amount=" + amount +
                ", date=" + date +
                ", memberID=" + (member != null ? member.getMemberID() : "null") +
                '}';
    }
}