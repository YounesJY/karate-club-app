package com.karateclub.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Create registry
                registry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();
// In the MetadataSources section of HibernateUtil.java
                MetadataSources sources = new MetadataSources(registry);

// Add ALL entity classes
                sources.addAnnotatedClass(com.karateclub.model.Person.class);
                sources.addAnnotatedClass(com.karateclub.model.BeltRank.class);
                sources.addAnnotatedClass(com.karateclub.model.Instructor.class);
                sources.addAnnotatedClass(com.karateclub.model.Member.class);
                sources.addAnnotatedClass(com.karateclub.model.Payment.class);
                sources.addAnnotatedClass(com.karateclub.model.BeltTest.class);
                sources.addAnnotatedClass(com.karateclub.model.SubscriptionPeriod.class);

                // Add entity classes here as we create them
                // sources.addAnnotatedClass(Person.class);
                // sources.addAnnotatedClass(Member.class);

                // Create Metadata
                Metadata metadata = sources.getMetadataBuilder().build();

                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}