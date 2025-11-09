package com.karateclub.dao;

import com.karateclub.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class GenericDAO<T> {
    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // CREATE - Save entity
    public void save(T entity) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // READ - Get by ID (lazy loading)
    public T getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(entityClass, id);
        }
    }

    // READ - Get all (lazy loading - PRODUCTION DEFAULT)
    public List<T> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName();
            Query<T> query = session.createQuery(hql, entityClass);
            return query.list();
        }
    }

    // UPDATE - Update entity
    public void update(T entity) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // DELETE - Delete entity
    public void delete(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            T entity = session.find(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    // Custom query with HQL (lazy loading)
    public List<T> findByHQL(String hql, Object... parameters) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery(hql, entityClass);
            for (int i = 0; i < parameters.length; i++) {
                query.setParameter(i + 1, parameters[i]);
            }
            return query.list();
        }
    }

    // Count all entities
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(*) FROM " + entityClass.getSimpleName();
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        }
    }

    // Check if entity exists by ID
    public boolean exists(int id) {
        return getById(id) != null;
    }

    // Get session for complex operations that need extended session
    protected Session getSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }
}