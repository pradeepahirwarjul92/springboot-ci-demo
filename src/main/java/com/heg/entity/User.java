package com.heg.entity;

import java.io.Serializable; // <-- 1. Add this import

// 2. Add "implements Serializable" to the class
public class User implements Serializable {
    
    // 3. Recommended: Add a serialVersionUID for version control of the object
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String email;

    // Existing Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Default constructor (required for serialization)
    public User() {
        super();
    }

    // Your existing constructor
    public User(Long id, String name, String email) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
    }
}