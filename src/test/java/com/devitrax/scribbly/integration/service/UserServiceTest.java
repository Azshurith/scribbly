package com.devitrax.scribbly.integration.service;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import com.devitrax.scribbly.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        userRepo.deleteAll(); // then delete parents
    }

    @BeforeEach
    void cleanUp() {
        userRepo.deleteAll();
    }

    @Test
    void shouldRegisterUserAndEncryptPasswordAndCache() {
        // Register user
        User user = userService.register("newuser", "plainpassword");

        // Check DB saved user
        User dbUser = userRepo.findById(user.getId()).orElse(null);
        assertThat(dbUser).isNotNull();
        assertThat(dbUser.getUsername()).isEqualTo("newuser");
        assertThat(passwordEncoder.matches("plainpassword", dbUser.getPassword())).isTrue();

        // Check password is encoded
        assertThat(dbUser.getPassword()).isNotEqualTo("plainpassword");

        // Check cache entry exists
        User cached = cacheManager.getCache("users").get(user.getId(), User.class);
        assertThat(cached).isNotNull();
        assertThat(cached.getUsername()).isEqualTo("newuser");
    }
}
