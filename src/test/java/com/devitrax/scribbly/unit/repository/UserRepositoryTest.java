package com.devitrax.scribbly.unit.repository;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    void shouldFindUserByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("secret");
        userRepo.save(user);

        // Act
        Optional<User> result = userRepo.findByUsername("johndoe");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("johndoe");
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // Act
        Optional<User> result = userRepo.findByUsername("nonexistent");

        // Assert
        assertThat(result).isNotPresent();
    }
}
