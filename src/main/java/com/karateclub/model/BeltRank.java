package com.karateclub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BeltRank")
public class BeltRank {
    @Id
    @Column(name = "RankID")
    private int rankID;

    @Column(name = "RankName", length = 50, nullable = false)
    private String rankName;

    @Column(name = "TestFees", nullable = false)
    private double testFees;

    // Constructors
    public BeltRank() {}

    public BeltRank(int rankID, String rankName, double testFees) {
        this.rankID = rankID;
        this.rankName = rankName;
        this.testFees = testFees;
    }

    // Getters and Setters
    public int getRankID() { return rankID; }
    public void setRankID(int rankID) { this.rankID = rankID; }

    public String getRankName() { return rankName; }
    public void setRankName(String rankName) { this.rankName = rankName; }

    public double getTestFees() { return testFees; }
    public void setTestFees(double testFees) { this.testFees = testFees; }

    @Override
    public String toString() {
        return "BeltRank{" +
                "rankID=" + rankID +
                ", rankName='" + rankName + '\'' +
                ", testFees=" + testFees +
                '}';
    }
}