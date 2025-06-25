package com.devitrax.scribbly.unit.controller;

import com.devitrax.scribbly.controller.RegisterController;
import com.devitrax.scribbly.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.Principal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RegisterControllerTest {

    private UserService userService;
    private RegisterController controller;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        controller = new RegisterController(userService);
    }

    @Test
    void shouldRedirectToPostListIfUserIsAuthenticated() {
        Principal principal = () -> "user123";

        String result = controller.index(principal);

        assertThat(result).isEqualTo("redirect:/post/list");
    }

    @Test
    void shouldShowRegisterPageIfUserNotAuthenticated() {
        String result = controller.index(null);

        assertThat(result).isEqualTo("authentication/register");
    }

    @Test
    void shouldRegisterUserAndRedirectToLogin() {
        String username = "newuser";
        String password = "securepass";

        String result = controller.register(username, password);

        verify(userService, times(1)).register(username, password);
        assertThat(result).isEqualTo("redirect:/login");
    }
}
