package com.karateclub.model;

public enum UserRole {
    MEMBER,      // Can view own profile and data
    INSTRUCTOR,  // Can view own data + assigned members
    ADMIN        // Full CRUD on everything
}
