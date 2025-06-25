package com.devitrax.scribbly.unit.config;

import com.devitrax.scribbly.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @Test
    void shouldReturnSecurityFilterChain() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        SecurityConfig config = new SecurityConfig();

        // Just verify that filterChain() doesn't throw and returns a non-null object.
        // Deep mocks avoid needing to mock internal DSL methods like authorizeHttpRequests().
        SecurityFilterChain chain = config.filterChain(httpSecurity);

        assertThat(chain).isNotNull();
    }

    @Test
    void shouldProvidePasswordEncoder() {
        SecurityConfig config = new SecurityConfig();
        var encoder = config.passwordEncoder();

        String rawPassword = "password";
        String encoded = encoder.encode(rawPassword);

        assertThat(encoder.matches(rawPassword, encoded)).isTrue();
    }
}
