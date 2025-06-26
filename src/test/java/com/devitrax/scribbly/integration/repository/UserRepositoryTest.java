package com.devitrax.scribbly.integration.repository;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindUserByUsername() {
        User user = new User();
        user.setUsername("user_" + UUID.randomUUID());
        user.setPassword("samplePass");
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername(user.getUsername());

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void shouldReturnEmptyIfUserNotFound() {
        Optional<User> result = userRepository.findByUsername("ghost");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldCheckIfUsernameIsUnique() {
        User user = new User();
        user.setUsername("user_" + UUID.randomUUID());
        user.setPassword("test123");
        userRepository.save(user);

        boolean exists = userRepository.findByUsername(user.getUsername()).isPresent();
        boolean notExists = userRepository.findByUsername("anotherUser").isPresent();

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
