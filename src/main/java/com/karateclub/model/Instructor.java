package com.karateclub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Instructor")
public class Instructor {
    @Id
    @Column(name = "InstructorID")
    private int instructorID;

    @OneToOne
    @JoinColumn(name = "PersonID", nullable = false)
    private Person person;

    @Column(name = "Qualification", length = 100)
    private String qualification;

    // Constructors
    public Instructor() {}

    public Instructor(int instructorID, Person person, String qualification) {
        this.instructorID = instructorID;
        this.person = person;
        this.qualification = qualification;
    }

    // Getters and Setters
    public int getInstructorID() { return instructorID; }
    public void setInstructorID(int instructorID) { this.instructorID = instructorID; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    @Override
    public String toString() {
        return "Instructor{" +
                "instructorID=" + instructorID +
                ", person=" + person +
                ", qualification='" + qualification + '\'' +
                '}';
    }
}