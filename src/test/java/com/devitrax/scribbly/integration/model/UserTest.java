package com.devitrax.scribbly.integration.model;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFindUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedpass");
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("testuser");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldUpdateUserPassword() {
        User user = new User();
        user.setUsername("secureuser");
        user.setPassword("oldpass");
        user = userRepository.save(user);

        user.setPassword("newpass");
        user = userRepository.save(user);

        User updated = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updated.getPassword()).isEqualTo("newpass");
    }

    @Test
    void shouldDeleteUser() {
        User user = new User();
        user.setUsername("tobedeleted");
        user.setPassword("pass");
        user = userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<User> deleted = userRepository.findById(user.getId());
        assertThat(deleted).isEmpty();
    }
}
