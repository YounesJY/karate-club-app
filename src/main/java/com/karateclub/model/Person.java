package com.karateclub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PersonID")
    private int personID;

    @Column(name = "Name", length = 100, nullable = false)
    private String name;

    @Column(name = "Address", length = 100)
    private String address;

    @Column(name = "ContactInfo", length = 100, nullable = false)
    private String contactInfo;

    // Constructors
    public Person() {}

    public Person(String name, String address, String contactInfo) {
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public int getPersonID() { return personID; }
    public void setPersonID(int personID) { this.personID = personID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    @Override
    public String toString() {
        return "Person{" +
                "personID=" + personID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}