// src/main/java/com/karateclub/dao/BaseDAO.java
package com.karateclub.dao;

import com.karateclub.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public abstract class BaseDAO {

    protected Session getSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    protected void executeInTransaction(TransactionOperation operation) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            operation.execute(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Transaction failed", e);
        }
    }

    protected <T> T executeQuery(QueryOperation<T> operation) {
        try (Session session = getSession()) {
            return operation.execute(session);
        } catch (Exception e) {
            throw new RuntimeException("Query failed", e);
        }
    }

    @FunctionalInterface
    public interface TransactionOperation {
        void execute(Session session);
    }

    @FunctionalInterface
    public interface QueryOperation<T> {
        T execute(Session session);
    }
}