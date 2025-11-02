package com.karateclub;

import com.karateclub.dao.*;
import com.karateclub.model.*;

import java.util.List;

public class TestDAOLayer {
    public static void main(String[] args) {
        System.out.println("🚀 Testing DAO Layer...\n");

        // Test PersonDAO
        PersonDAO personDAO = new PersonDAO();
        System.out.println("=== Testing PersonDAO ===");
        List<Person> allPeople = personDAO.getAll();
        System.out.println("Total people: " + allPeople.size());

        List<Person> johns = personDAO.findByName("John");
        System.out.println("People named John: " + johns.size());

        // Test MemberDAO
        MemberDAO memberDAO = new MemberDAO();
        System.out.println("\n=== Testing MemberDAO ===");
        List<Member> activeMembers = memberDAO.findActiveMembers();
        System.out.println("Active members: " + activeMembers.size());

        List<Member> whiteBelts = memberDAO.findByBeltRank(1);
        System.out.println("White belt members: " + whiteBelts.size());

        // Test BeltRankDAO
        BeltRankDAO beltRankDAO = new BeltRankDAO();
        System.out.println("\n=== Testing BeltRankDAO ===");
        List<BeltRank> allRanks = beltRankDAO.getAllOrdered();
        System.out.println("All belt ranks:");
        for (BeltRank rank : allRanks) {
            System.out.println(" - " + rank.getRankName() + " ($" + rank.getTestFees() + ")");
        }

        // Test CRUD operations
        System.out.println("\n=== Testing CRUD Operations ===");

        // CREATE - Add new person
        Person newPerson = new Person(7, "Test User", "Test Address", "555-9999");
        personDAO.save(newPerson);
        System.out.println("✅ Created new person: " + newPerson.getName());

        // READ - Get by ID
        Person retrievedPerson = personDAO.getById(7);
        System.out.println("✅ Retrieved person: " + retrievedPerson.getName());

        // UPDATE - Modify person
        retrievedPerson.setAddress("Updated Address");
        personDAO.update(retrievedPerson);
        System.out.println("✅ Updated person address");

        // DELETE - Remove person
        personDAO.delete(7);
        System.out.println("✅ Deleted test person");

        // Verify deletion
        Person deletedPerson = personDAO.getById(7);
        System.out.println("✅ Person after deletion: " + (deletedPerson == null ? "null (correct)" : "still exists"));

        System.out.println("\n🎉 DAO Layer Testing Complete!");
    }
}