package com.karateclub;

import com.karateclub.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TestHibernate {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("✅ Hibernate connection successful!");
            System.out.println("✅ Database: " + session.getSessionFactory().getProperties().get("hibernate.connection.url"));
        } catch (Exception e) {
            System.out.println("❌ Hibernate connection failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}