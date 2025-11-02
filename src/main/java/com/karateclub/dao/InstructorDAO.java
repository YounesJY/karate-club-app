package com.karateclub.dao;

import com.karateclub.model.Instructor;

public class InstructorDAO extends GenericDAO<Instructor> {

    public InstructorDAO() {
        super(Instructor.class);
    }

    // Custom methods can be added here as needed
    // For now, we inherit all CRUD from GenericDAO
}