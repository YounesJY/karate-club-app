package com.karateclub.tests;

import com.karateclub.dao.*;
import com.karateclub.model.*;

import java.util.List;

public class TestDAOSimple {
    public static void main(String[] args) {
        System.out.println("🚀 Testing DAO Layer (Simple)...\n");

        try {
            // Test PersonDAO
            PersonDAO personDAO = new PersonDAO();
            System.out.println("=== Testing PersonDAO ===");
            List<Person> allPeople = personDAO.getAll();
            System.out.println("✅ Total people: " + allPeople.size());

            List<Person> johns = personDAO.findByName("John");
            System.out.println("✅ People named John: " + johns.size());

            // Test MemberDAO
            MemberDAO memberDAO = new MemberDAO();
            System.out.println("\n=== Testing MemberDAO ===");
            List<Member> activeMembers = memberDAO.findActiveMembers();
            System.out.println("✅ Active members: " + activeMembers.size());

            List<Member> whiteBelts = memberDAO.findByBeltRank(1);
            System.out.println("✅ White belt members: " + whiteBelts.size());

            // Test BeltRankDAO
            BeltRankDAO beltRankDAO = new BeltRankDAO();
            System.out.println("\n=== Testing BeltRankDAO ===");
            List<BeltRank> allRanks = beltRankDAO.getAllOrdered();
            System.out.println("✅ All belt ranks: " + allRanks.size());
            for (BeltRank rank : allRanks) {
                System.out.println(" - " + rank.getRankName() + " ($" + rank.getTestFees() + ")");
            }

            // Test basic CRUD
            System.out.println("\n=== Testing Basic CRUD ===");

            // Test getting a single member with details
            if (!activeMembers.isEmpty()) {
                Member firstMember = activeMembers.get(0);
                System.out.println("✅ First active member: " + firstMember.getPerson().getName());
                System.out.println("✅ Belt rank: " + firstMember.getLastBeltRank().getRankName());
            }

            System.out.println("\n🎉 DAO Layer Testing Complete!");

        } catch (Exception e) {
            System.out.println("❌ Error during DAO testing: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure Hibernate is shutdown
            com.karateclub.config.HibernateUtil.shutdown();
        }
    }
}