package com.karateclub.tests;

import com.karateclub.config.HibernateUtil;
import org.hibernate.Session;

public class TestEntities {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("✅ All entities registered successfully!");
            System.out.println("✅ Session factory created with all mappings!");

            // Test that we can create a transaction
            session.beginTransaction();
            System.out.println("✅ Transaction started successfully!");
            session.getTransaction().rollback(); // Just rollback since we're only testing
            System.out.println("✅ Transaction rollback successful!");

        } catch (Exception e) {
            System.out.println("❌ Error with entities: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}