// src/main/java/com/karateclub/dao/UserDAO.java
package com.karateclub.dao;

import com.karateclub.model.User;
import com.karateclub.model.UserRole;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class UserDAO extends GenericDAO<User> {

    public UserDAO() {
        super(User.class);
    }

    // Find by username (for login)
    public User findByUsername(String username) {
        try (Session session = getSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        }
    }

    // Find by Person ID
    public User findByPersonId(int personId) {  // Changed to int
        try (Session session = getSession()) {
            Query<User> query = session.createQuery("FROM User WHERE person.personID = :personId", User.class);
            query.setParameter("personId", personId);
            return query.uniqueResult();
        }
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        try (Session session = getSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE username = :username", Long.class);
            query.setParameter("username", username);
            return query.uniqueResult() > 0;
        }
    }

    // Get all users sorted by username
    public List<User> findAllSorted() {
        try (Session session = getSession()) {
            Query<User> query = session.createQuery("FROM User ORDER BY username", User.class);
            return query.list();
        }
    }

    // Find users by role
    public List<User> findByRole(UserRole role) {  // Changed from String to UserRole
        try (Session session = getSession()) {
            Query<User> query = session.createQuery("FROM User WHERE primaryRole = :role ORDER BY username", User.class);
            query.setParameter("role", role);  // Now passes UserRole directly
            return query.list();
        }
    }
}