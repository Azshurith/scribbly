package com.devitrax.scribbly.unit.model;

import com.devitrax.scribbly.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void shouldSetAndGetUsername() {
        User user = new User();
        user.setUsername("scribbler");

        assertThat(user.getUsername()).isEqualTo("scribbler");
    }

    @Test
    void shouldSetAndGetPassword() {
        User user = new User();
        user.setPassword("encrypted");

        assertThat(user.getPassword()).isEqualTo("encrypted");
    }

    @Test
    void shouldSetAndGetId() {
        User user = new User();

        // Simulate JPA setting the ID
        Long id = 99L;
        user.setUsername("test");
        user.setPassword("pass");

        // Use reflection to simulate ID (optional: use Lombok or builder pattern)
        try {
            var field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(user.getId()).isEqualTo(99L);
    }
}
