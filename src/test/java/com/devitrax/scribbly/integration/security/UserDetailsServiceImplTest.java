package com.devitrax.scribbly.integration.security;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import com.devitrax.scribbly.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserDetailsServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("sampleUser");
        user.setPassword("encryptedPass");
        userRepository.save(user);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("sampleUser");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("sampleUser");
        assertThat(userDetails.getPassword()).isEqualTo("encryptedPass");
        assertThat(userDetails.getAuthorities()).extracting("authority").contains("ROLE_USER");
    }

    @Test
    void shouldThrowExceptionForUnknownUser() {
        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("ghost")
        );
    }
}
