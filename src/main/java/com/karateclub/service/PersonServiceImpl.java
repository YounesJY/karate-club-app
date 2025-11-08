package com.karateclub.service;

import com.karateclub.dao.PersonDAO;
import com.karateclub.model.Person;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;

import java.util.List;

public class PersonServiceImpl implements PersonService {
    private PersonDAO personDAO;

    public PersonServiceImpl() {
        this.personDAO = new PersonDAO();
    }

    @Override
    public Person getPersonById(int personID) {
        validatePersonID(personID);

        Person person = personDAO.getById(personID);
        if (person == null) {
            throw new NotFoundException("Person not found with ID: " + personID);
        }
        return person;
    }

    @Override
    public List<Person> getAllPeople() {
        return personDAO.getAll();
    }

    @Override
    public Person createPerson(Person person) {
        validatePerson(person);

        // Check for duplicate contact info
        List<Person> existing = personDAO.findByContactInfo(person.getContactInfo());
        if (!existing.isEmpty()) {
            throw new ValidationException("Person with contact info '" + person.getContactInfo() + "' already exists");
        }

        personDAO.save(person);
        return person;
    }

    @Override
    public Person updatePerson(Person person) {
        validatePerson(person);
        validatePersonExists(person.getPersonID());

        personDAO.update(person);
        return person;
    }

    @Override
    public void deletePerson(int personID) {
        validatePersonID(personID);
        validatePersonExists(personID);

        // In a real application, we would check if person is used elsewhere
        // For now, we'll allow deletion
        personDAO.delete(personID);
    }

    @Override
    public List<Person> findPeopleByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be empty for search");
        }
        return personDAO.findByName(name);
    }

    @Override
    public List<Person> findPeopleByContactInfo(String contactInfo) {
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            throw new ValidationException("Contact info cannot be empty for search");
        }
        return personDAO.findByContactInfo(contactInfo);
    }

    @Override
    public boolean isPersonExists(int personID) {
        validatePersonID(personID);
        return personDAO.getById(personID) != null;
    }

    // Private validation methods
    private void validatePersonID(int personID) {
        if (personID <= 0) {
            throw new ValidationException("Person ID must be positive");
        }
    }

    private void validatePerson(Person person) {
        if (person == null) {
            throw new ValidationException("Person cannot be null");
        }
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new ValidationException("Person name is required");
        }
        if (person.getName().trim().length() < 2) {
            throw new ValidationException("Person name must be at least 2 characters");
        }
        if (person.getContactInfo() == null || person.getContactInfo().trim().isEmpty()) {
            throw new ValidationException("Contact info is required");
        }
    }

    private void validatePersonExists(int personID) {
        if (!isPersonExists(personID)) {
            throw new NotFoundException("Person not found with ID: " + personID);
        }
    }
}