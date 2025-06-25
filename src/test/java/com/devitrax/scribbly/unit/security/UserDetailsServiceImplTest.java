package com.devitrax.scribbly.unit.security;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import com.devitrax.scribbly.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @Test
    void loadUserByUsername_returnsUserDetails_whenUserExists() {
        // Arrange
        UserRepository userRepo = mock(UserRepository.class);
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedpassword");

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepo);

        // Act
        UserDetails result = service.loadUserByUsername("testuser");

        // Assert
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedpassword", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_throwsException_whenUserNotFound() {
        // Arrange
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.findByUsername("missing")).thenReturn(Optional.empty());

        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepo);

        // Act + Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("missing");
        });
    }
}
