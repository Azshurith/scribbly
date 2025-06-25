package com.devitrax.scribbly.integration.security;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserExistenceFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("validuser");
        user.setPassword("password");
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "validuser")
    void shouldAllowAccessIfUserExists() throws Exception {
        mockMvc.perform(get("/post/list"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "deleteduser")
    void shouldRedirectIfUserNoLongerExists() throws Exception {
        mockMvc.perform(get("/post/list"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?forceLogout=true"));
    }

    @Test
    void shouldAllowPublicAccess() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());
    }
}
