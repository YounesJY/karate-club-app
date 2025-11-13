// src/main/java/com/karateclub/model/User.java
package com.karateclub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private int userID;

    @Column(name = "Username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "PrimaryRole", nullable = false)
    private UserRole primaryRole;

    // In User.java - update the Person reference
    @OneToOne
    @JoinColumn(name = "PersonID", nullable = false)
    private Person person;

    @Column(name = "CreatedAt")
    private java.sql.Timestamp createdAt;

    // Constructors
    public User() {}

    public User(String username, String password, UserRole primaryRole, Person person) {
        this.username = username;
        this.password = password;
        this.primaryRole = primaryRole;
        this.person = person;
        this.createdAt = new java.sql.Timestamp(System.currentTimeMillis());
    }

    // Helper methods for role checking
    public boolean isAdmin() {
        return primaryRole == UserRole.ADMIN;
    }

    public boolean isInstructor() {
        return primaryRole == UserRole.INSTRUCTOR || primaryRole == UserRole.ADMIN;
    }

    public boolean isMember() {
        return true;
    }

    // Getters and Setters (updated naming)
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getPrimaryRole() { return primaryRole; }
    public void setPrimaryRole(UserRole primaryRole) { this.primaryRole = primaryRole; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public java.sql.Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.sql.Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", primaryRole=" + primaryRole +
                ", person=" + (person != null ? person.getName() : "null") +
                '}';
    }
}