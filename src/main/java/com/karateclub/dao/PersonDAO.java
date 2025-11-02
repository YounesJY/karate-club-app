package com.karateclub.dao;

import com.karateclub.config.HibernateUtil;
import com.karateclub.model.Person;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PersonDAO extends GenericDAO<Person> {

    public PersonDAO() {
        super(Person.class);
    }

    // Custom method: Find person by name
    public List<Person> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Person p WHERE p.name LIKE :name";
            Query<Person> query = session.createQuery(hql, Person.class);
            query.setParameter("name", "%" + name + "%");
            return query.list();
        }
    }

    // Custom method: Find by contact info
    public List<Person> findByContactInfo(String contactInfo) {
        return findByHQL("FROM Person p WHERE p.contactInfo LIKE ?1", "%" + contactInfo + "%");
    }
}