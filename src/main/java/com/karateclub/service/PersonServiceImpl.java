package com.karateclub.service;

import com.karateclub.dao.PersonDAO;
import com.karateclub.model.Person;

import java.util.List;

public class PersonServiceImpl implements PersonService {
    private PersonDAO personDAO;

    public PersonServiceImpl() {
        this.personDAO = new PersonDAO();
    }

    @Override
    public Person getPersonById(int personID) {
        return personDAO.getById(personID);
    }

    @Override
    public List<Person> getAllPeople() {
        return personDAO.getAll();
    }

    @Override
    public Person createPerson(Person person) {
        validatePerson(person);

        // Business rule: Check for duplicate contact info
        List<Person> existing = personDAO.findByContactInfo(person.getContactInfo());
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException("Person with this contact info already exists");
        }

        personDAO.save(person);
        return person;
    }

    @Override
    public Person updatePerson(Person person) {
        validatePerson(person);
        personDAO.update(person);
        return person;
    }

    @Override
    public void deletePerson(int personID) {
        Person person = personDAO.getById(personID);
        if (person != null) {
            // Business rule: Check if person is associated with any member/instructor
            // This would require additional checks in a real application
            personDAO.delete(personID);
        }
    }

    @Override
    public List<Person> findPeopleByName(String name) {
        return personDAO.findByName(name);
    }

    @Override
    public List<Person> findPeopleByContactInfo(String contactInfo) {
        return personDAO.findByContactInfo(contactInfo);
    }

    private void validatePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Person name is required");
        }
        if (person.getContactInfo() == null || person.getContactInfo().trim().isEmpty()) {
            throw new IllegalArgumentException("Contact info is required");
        }
    }
}