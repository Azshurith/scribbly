package com.devitrax.scribbly.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a user in the Scribbly application.
 */
@Entity
@Table(name = "users")
public class User {

    /** The unique identifier of the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The unique username used for login and identification. */
    @Column(unique = true)
    private String username;

    /** The user's encrypted password. */
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    /**
     * Gets the user's ID.
     * @return the ID of the user.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the user's username.
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username.
     * @param username the new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user's password.
     * @return the encrypted password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * @param password the encrypted password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

}