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

            // 1. Create Belt Ranks
            System.out.println("Creating belt ranks...");
            BeltRank whiteBelt = new BeltRank(1, "White Belt", 50.0);
            BeltRank yellowBelt = new BeltRank(2, "Yellow Belt", 60.0);
            BeltRank orangeBelt = new BeltRank(3, "Orange Belt", 70.0);
            BeltRank greenBelt = new BeltRank(4, "Green Belt", 80.0);
            BeltRank blueBelt = new BeltRank(5, "Blue Belt", 90.0);
            BeltRank brownBelt = new BeltRank(6, "Brown Belt", 100.0);
            BeltRank blackBelt = new BeltRank(7, "Black Belt", 150.0);

            session.persist(whiteBelt);
            session.persist(yellowBelt);
            session.persist(orangeBelt);
            session.persist(greenBelt);
            session.persist(blueBelt);
            session.persist(brownBelt);
            session.persist(blackBelt);

            // 2. Create People
            System.out.println("Creating people...");
            Person person1 = new Person(1, "John Doe", "123 Main St", "555-0101");
            Person person2 = new Person(2, "Jane Smith", "456 Oak Ave", "555-0102");
            Person person3 = new Person(3, "Mike Johnson", "789 Pine Rd", "555-0103");
            Person person4 = new Person(4, "Sarah Wilson", "321 Elm St", "555-0104");
            Person person5 = new Person(5, "Master Chen", "654 Maple Dr", "555-0105");
            Person person6 = new Person(6, "Sensei Yamada", "987 Birch Ln", "555-0106");

            session.persist(person1);
            session.persist(person2);
            session.persist(person3);
            session.persist(person4);
            session.persist(person5);
            session.persist(person6);

            // 3. Create Instructors
            System.out.println("Creating instructors...");
            Instructor instructor1 = new Instructor(1, person5, "5th Dan Black Belt");
            Instructor instructor2 = new Instructor(2, person6, "4th Dan Black Belt");

            session.persist(instructor1);
            session.persist(instructor2);

            // 4. Create Members
            System.out.println("Creating members...");
            Member member1 = new Member(1, person1, "Emergency: 555-9111", whiteBelt, true);
            Member member2 = new Member(2, person2, "Emergency: 555-9112", yellowBelt, true);
            Member member3 = new Member(3, person3, "Emergency: 555-9113", orangeBelt, true);
            Member member4 = new Member(4, person4, "Emergency: 555-9114", greenBelt, false);

            session.persist(member1);
            session.persist(member2);
            session.persist(member3);
            session.persist(member4);

            // 5. Create Payments
            System.out.println("Creating payments...");
            Payment payment1 = new Payment(1, 100.0, LocalDate.now().minusDays(30), member1);
            Payment payment2 = new Payment(2, 120.0, LocalDate.now().minusDays(15), member2);
            Payment payment3 = new Payment(3, 80.0, LocalDate.now(), member3);

            session.persist(payment1);
            session.persist(payment2);
            session.persist(payment3);

            // 6. Create Belt Tests
            System.out.println("Creating belt tests...");
            BeltTest test1 = new BeltTest(1, member1, whiteBelt, true,
                    LocalDate.now().minusDays(60), instructor1, payment1);
            BeltTest test2 = new BeltTest(2, member2, yellowBelt, true,
                    LocalDate.now().minusDays(30), instructor2, payment2);
            BeltTest test3 = new BeltTest(3, member3, orangeBelt, false,
                    LocalDate.now().minusDays(10), instructor1, null);

            session.persist(test1);
            session.persist(test2);
            session.persist(test3);

            // 7. Create Subscription Periods
            System.out.println("Creating subscription periods...");
            SubscriptionPeriod sub1 = new SubscriptionPeriod(1,
                    LocalDateTime.now().minusMonths(3),
                    LocalDateTime.now().minusMonths(2),
                    200.0, true, member1, payment1);

            SubscriptionPeriod sub2 = new SubscriptionPeriod(2,
                    LocalDateTime.now().minusMonths(1),
                    LocalDateTime.now().plusMonths(2),
                    200.0, true, member2, payment2);

            SubscriptionPeriod sub3 = new SubscriptionPeriod(3,
                    LocalDateTime.now().minusDays(15),
                    LocalDateTime.now().plusMonths(1),
                    200.0, false, member3, null);

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