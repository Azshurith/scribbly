package com.devitrax.scribbly.unit.controller;

import com.devitrax.scribbly.controller.LoginController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerTest {

    @Test
    void shouldReturnLoginView() {
        LoginController controller = new LoginController();
        String result = controller.index();
        assertThat(result).isEqualTo("authentication/login");
    }
}
