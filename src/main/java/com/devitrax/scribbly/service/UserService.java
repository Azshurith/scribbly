package com.devitrax.scribbly.service;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling user-related operations,
 * including registration and password encryption.
 */
@Service
public class UserService {

    /** Repository for accessing user data. */
    private final UserRepository userRepo;

    /** Password encoder for securely storing passwords. */
    private final PasswordEncoder encoder;

    /**
     * Constructs a UserService with the given repository and password encoder.
     *
     * @param userRepo the user repository
     * @param encoder the password encoder
     */
    public UserService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    /**
     * Registers a new user by saving their username and encrypted password.
     * The result is cached under the user's ID.
     *
     * @param username the user's chosen username
     * @param password the user's plain-text password
     * @return the saved {@link User} entity
     */
    @CachePut(value = "users", key = "#result.id")
    public User register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        return userRepo.save(user);
    }
}
