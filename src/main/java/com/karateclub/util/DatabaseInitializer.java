package com.karateclub.util;

import com.karateclub.config.HibernateUtil;
import com.karateclub.model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DatabaseInitializer {

    public static void initializeSampleData() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            System.out.println("🚀 Starting database initialization...");

            // 1. Create Belt Ranks - NO IDs in constructors
            System.out.println("Creating belt ranks...");
            BeltRank whiteBelt = new BeltRank();
            whiteBelt.setRankName("White Belt");
            whiteBelt.setTestFees(50.0);

            BeltRank yellowBelt = new BeltRank();
            yellowBelt.setRankName("Yellow Belt");
            yellowBelt.setTestFees(60.0);

            BeltRank orangeBelt = new BeltRank();
            orangeBelt.setRankName("Orange Belt");
            orangeBelt.setTestFees(70.0);

            BeltRank greenBelt = new BeltRank();
            greenBelt.setRankName("Green Belt");
            greenBelt.setTestFees(80.0);

            BeltRank blueBelt = new BeltRank();
            blueBelt.setRankName("Blue Belt");
            blueBelt.setTestFees(90.0);

            BeltRank brownBelt = new BeltRank();
            brownBelt.setRankName("Brown Belt");
            brownBelt.setTestFees(100.0);

            BeltRank blackBelt = new BeltRank();
            blackBelt.setRankName("Black Belt");
            blackBelt.setTestFees(150.0);

            session.persist(whiteBelt);
            session.persist(yellowBelt);
            session.persist(orangeBelt);
            session.persist(greenBelt);
            session.persist(blueBelt);
            session.persist(brownBelt);
            session.persist(blackBelt);

            // 2. Create People - NO IDs
            System.out.println("Creating people...");
            Person person1 = new Person();
            person1.setName("John Doe");
            person1.setAddress("123 Main St");
            person1.setContactInfo("555-0101");

            Person person2 = new Person();
            person2.setName("Jane Smith");
            person2.setAddress("456 Oak Ave");
            person2.setContactInfo("555-0102");

            Person person3 = new Person();
            person3.setName("Mike Johnson");
            person3.setAddress("789 Pine Rd");
            person3.setContactInfo("555-0103");

            Person person4 = new Person();
            person4.setName("Sarah Wilson");
            person4.setAddress("321 Elm St");
            person4.setContactInfo("555-0104");

            Person person5 = new Person();
            person5.setName("Master Chen");
            person5.setAddress("654 Maple Dr");
            person5.setContactInfo("555-0105");

            Person person6 = new Person();
            person6.setName("Sensei Yamada");
            person6.setAddress("987 Birch Ln");
            person6.setContactInfo("555-0106");

            session.persist(person1);
            session.persist(person2);
            session.persist(person3);
            session.persist(person4);
            session.persist(person5);
            session.persist(person6);

            // 3. Create Instructors - NO IDs
            System.out.println("Creating instructors...");
            Instructor instructor1 = new Instructor();
            instructor1.setPerson(person5);
            instructor1.setQualification("5th Dan Black Belt");

            Instructor instructor2 = new Instructor();
            instructor2.setPerson(person6);
            instructor2.setQualification("4th Dan Black Belt");

            session.persist(instructor1);
            session.persist(instructor2);

            // 4. Create Members - NO IDs
            System.out.println("Creating members...");
            Member member1 = new Member();
            member1.setPerson(person1);
            member1.setEmergencyContactInfo("Emergency: 555-9111");
            member1.setLastBeltRank(whiteBelt);
            member1.setActive(true);

            Member member2 = new Member();
            member2.setPerson(person2);
            member2.setEmergencyContactInfo("Emergency: 555-9112");
            member2.setLastBeltRank(yellowBelt);
            member2.setActive(true);

            Member member3 = new Member();
            member3.setPerson(person3);
            member3.setEmergencyContactInfo("Emergency: 555-9113");
            member3.setLastBeltRank(orangeBelt);
            member3.setActive(true);

            Member member4 = new Member();
            member4.setPerson(person4);
            member4.setEmergencyContactInfo("Emergency: 555-9114");
            member4.setLastBeltRank(greenBelt);
            member4.setActive(false);

            session.persist(member1);
            session.persist(member2);
            session.persist(member3);
            session.persist(member4);

            // 5. Create Payments - NO IDs
            System.out.println("Creating payments...");
            Payment payment1 = new Payment();
            payment1.setAmount(100.0);
            payment1.setDate(LocalDate.now().minusDays(30));
            payment1.setMember(member1);

            Payment payment2 = new Payment();
            payment2.setAmount(120.0);
            payment2.setDate(LocalDate.now().minusDays(15));
            payment2.setMember(member2);

            Payment payment3 = new Payment();
            payment3.setAmount(80.0);
            payment3.setDate(LocalDate.now());
            payment3.setMember(member3);

            session.persist(payment1);
            session.persist(payment2);
            session.persist(payment3);

            // 6. Create Belt Tests - NO IDs
            System.out.println("Creating belt tests...");
            BeltTest test1 = new BeltTest();
            test1.setMember(member1);
            test1.setRank(whiteBelt);
            test1.setResult(true);
            test1.setDate(LocalDate.now().minusDays(60));
            test1.setTestedByInstructor(instructor1);
            test1.setPayment(payment1);

            BeltTest test2 = new BeltTest();
            test2.setMember(member2);
            test2.setRank(yellowBelt);
            test2.setResult(true);
            test2.setDate(LocalDate.now().minusDays(30));
            test2.setTestedByInstructor(instructor2);
            test2.setPayment(payment2);

            BeltTest test3 = new BeltTest();
            test3.setMember(member3);
            test3.setRank(orangeBelt);
            test3.setResult(false);
            test3.setDate(LocalDate.now().minusDays(10));
            test3.setTestedByInstructor(instructor1);
            test3.setPayment(null);

            session.persist(test1);
            session.persist(test2);
            session.persist(test3);

            // 7. Create Subscription Periods - NO IDs
            System.out.println("Creating subscription periods...");
            SubscriptionPeriod sub1 = new SubscriptionPeriod();
            sub1.setStartDate(LocalDateTime.now().minusMonths(3));
            sub1.setEndDate(LocalDateTime.now().minusMonths(2));
            sub1.setFees(200.0);
            sub1.setPaid(true);
            sub1.setMember(member1);
            sub1.setPayment(payment1);

            SubscriptionPeriod sub2 = new SubscriptionPeriod();
            sub2.setStartDate(LocalDateTime.now().minusMonths(1));
            sub2.setEndDate(LocalDateTime.now().plusMonths(2));
            sub2.setFees(200.0);
            sub2.setPaid(true);
            sub2.setMember(member2);
            sub2.setPayment(payment2);

            SubscriptionPeriod sub3 = new SubscriptionPeriod();
            sub3.setStartDate(LocalDateTime.now().minusDays(15));
            sub3.setEndDate(LocalDateTime.now().plusMonths(1));
            sub3.setFees(200.0);
            sub3.setPaid(false);
            sub3.setMember(member3);
            sub3.setPayment(null);

            session.persist(sub1);
            session.persist(sub2);
            session.persist(sub3);

            // 8. Create Member-Instructor assignments
            System.out.println("Creating member-instructor assignments...");
            MemberInstructor assignment1 = new MemberInstructor(member1, instructor1, LocalDateTime.now().minusMonths(2));
            MemberInstructor assignment2 = new MemberInstructor(member2, instructor2, LocalDateTime.now().minusMonths(1));
            MemberInstructor assignment3 = new MemberInstructor(member3, instructor1, LocalDateTime.now().minusDays(15));
            MemberInstructor assignment4 = new MemberInstructor(member3, instructor2, LocalDateTime.now().minusDays(10));

            session.persist(assignment1);
            session.persist(assignment2);
            session.persist(assignment3);
            session.persist(assignment4);

            transaction.commit();
            System.out.println("✅ Database initialized successfully with sample data!");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("❌ Error initializing database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void main(String[] args) {
        initializeSampleData();
    }
}