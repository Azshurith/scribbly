package com.devitrax.scribbly.integration.repository;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindUserByUsername() {
        User user = new User();
        user.setUsername("sampleUser");
        user.setPassword("samplePass");
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("sampleUser");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("sampleUser");
    }

    @Test
    void shouldReturnEmptyIfUserNotFound() {
        Optional<User> result = userRepository.findByUsername("ghost");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldCheckIfUsernameIsUnique() {
        User user = new User();
        user.setUsername("uniqueUser");
        user.setPassword("test123");
        userRepository.save(user);

        boolean exists = userRepository.findByUsername("uniqueUser").isPresent();
        boolean notExists = userRepository.findByUsername("anotherUser").isPresent();

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
