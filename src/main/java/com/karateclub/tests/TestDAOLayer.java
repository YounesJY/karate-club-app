package com.karateclub.tests;

import com.karateclub.dao.*;
import com.karateclub.model.*;
import com.karateclub.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TestDAOLayer {
    public static void main(String[] args) {
        System.out.println("🚀 Testing DAO Layer...\n");

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

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

            // CREATE - Add new person using setters (NO ID in constructor)
            Person newPerson = new Person();
            newPerson.setName("Test User");
            newPerson.setAddress("Test Address");
            newPerson.setContactInfo("555-9999");
            personDAO.save(newPerson);
            System.out.println("✅ Created new person: " + newPerson.getName() + " (ID: " + newPerson.getPersonID() + ")");

            // READ - Get by ID
            Person retrievedPerson = personDAO.getById(newPerson.getPersonID());
            System.out.println("✅ Retrieved person: " + retrievedPerson.getName());

            // UPDATE - Modify person
            retrievedPerson.setAddress("Updated Address");
            personDAO.update(retrievedPerson);
            System.out.println("✅ Updated person address");

            // DELETE - Remove person
            personDAO.delete(newPerson.getPersonID());
            System.out.println("✅ Deleted test person");

            // Verify deletion
            Person deletedPerson = personDAO.getById(newPerson.getPersonID());
            System.out.println("✅ Person after deletion: " + (deletedPerson == null ? "null (correct)" : "still exists"));

            transaction.commit();
            System.out.println("\n🎉 DAO Layer Testing Complete!");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("❌ DAO Layer Test Failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}