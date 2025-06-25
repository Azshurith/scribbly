package com.devitrax.scribbly.unit.service;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import com.devitrax.scribbly.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldEncodePasswordAndSaveUser() {
        String rawPassword = "plain";
        String encoded = "encoded";

        when(encoder.encode(rawPassword)).thenReturn(encoded);
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.register("testuser", rawPassword);

        assertEquals("testuser", user.getUsername());
        assertEquals("encoded", user.getPassword());
        verify(userRepo).save(any(User.class));
    }
}
