package com.karateclub.service;

import com.karateclub.model.Person;
import java.util.List;

public interface PersonService {

    // Basic CRUD operations
    Person getPersonById(int personID);
    List<Person> getAllPeople();
    Person createPerson(Person person);
    Person updatePerson(Person person);
    void deletePerson(int personID);

    // Query operations
    List<Person> findPeopleByName(String name);
    List<Person> findPeopleByContactInfo(String contactInfo);

    // Validation
    boolean isPersonExists(int personID);
}