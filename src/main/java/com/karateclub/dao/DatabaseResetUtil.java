package com.karateclub.dao;

import com.karateclub.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class to reset AUTO_INCREMENT counters in the database.
 * Useful when you want IDs to start from 1 after clearing tables.
 */
public class DatabaseResetUtil {

    // List of all tables in the database
    private static final List<String> TABLES = Arrays.asList(
        "BeltTest",
        "MemberInstructor",
        "SubscriptionPeriods",
        "Payment",
        "Member",
        "Instructor",
        "Person",
        "BeltRank"
    );

    /**
     * Reset AUTO_INCREMENT counter for all tables to 1.
     * WARNING: This should only be used when tables are empty!
     */
    public static void resetAllAutoIncrements() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            for (String tableName : TABLES) {
                resetAutoIncrement(session, tableName);
            }

            transaction.commit();
            System.out.println("Successfully reset AUTO_INCREMENT for all tables.");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error resetting AUTO_INCREMENT: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Reset AUTO_INCREMENT counter for a specific table to 1.
     *
     * @param tableName The name of the table to reset
     */
    public static void resetAutoIncrement(String tableName) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            resetAutoIncrement(session, tableName);

            transaction.commit();
            System.out.println("Successfully reset AUTO_INCREMENT for table: " + tableName);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error resetting AUTO_INCREMENT for " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Reset AUTO_INCREMENT counter using an existing session.
     *
     * @param session The Hibernate session to use
     * @param tableName The name of the table to reset
     */
    private static void resetAutoIncrement(Session session, String tableName) {
        String sql = "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1";
        session.createNativeMutationQuery(sql).executeUpdate();
        System.out.println("Reset AUTO_INCREMENT for: " + tableName);
    }

    /**
     * Delete all data from all tables and reset AUTO_INCREMENT counters.
     * WARNING: This will permanently delete all data!
     */
    public static void clearAllTablesAndReset() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Disable foreign key checks temporarily
            session.createNativeMutationQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

            // Delete all data from tables (in reverse order to handle foreign keys)
            List<String> reverseTables = Arrays.asList(
                "BeltTest",
                "MemberInstructor",
                "SubscriptionPeriods",
                "Payment",
                "Member",
                "Instructor",
                "Person",
                "BeltRank"
            );

            for (String tableName : reverseTables) {
                session.createNativeMutationQuery("DELETE FROM " + tableName).executeUpdate();
                System.out.println("Cleared table: " + tableName);
            }

            // Reset AUTO_INCREMENT for all tables
            for (String tableName : TABLES) {
                resetAutoIncrement(session, tableName);
            }

            // Re-enable foreign key checks
            session.createNativeMutationQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

            transaction.commit();
            System.out.println("Successfully cleared all tables and reset AUTO_INCREMENT counters.");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error clearing tables: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Main method for testing or running manually.
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            String command = args[0].toLowerCase();

            switch (command) {
                case "reset":
                    // Reset AUTO_INCREMENT only (tables must be empty)
                    resetAllAutoIncrements();
                    break;

                case "clear":
                    // Clear all data and reset AUTO_INCREMENT
                    System.out.println("WARNING: This will delete all data!");
                    System.out.println("Are you sure? (Uncomment the line below to proceed)");
                    // clearAllTablesAndReset();
                    break;

                default:
                    System.out.println("Usage: java DatabaseResetUtil [reset|clear]");
                    System.out.println("  reset - Reset AUTO_INCREMENT counters (tables must be empty)");
                    System.out.println("  clear - Clear all data and reset AUTO_INCREMENT");
            }
        } else {
            System.out.println("Usage: java DatabaseResetUtil [reset|clear]");
            System.out.println("  reset - Reset AUTO_INCREMENT counters (tables must be empty)");
            System.out.println("  clear - Clear all data and reset AUTO_INCREMENT");
        }
    }
}

