package com.devitrax.scribbly.integration.config;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.PostRepository;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Long testPostId;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user = userRepository.save(user);

        Post post = new Post();
        post.setTitle("Public Post");
        post.setContent("This is a public post");
        post.setUser(user);
        testPostId = postRepository.save(post).getId();
    }

    @Test
    void shouldAllowAccessToLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldRedirectToLoginForProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/post/new"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void shouldAllowAccessToPublicPostView() throws Exception {
        mockMvc.perform(get("/post/" + testPostId))
            .andExpect(status().isOk())
            .andExpect(view().name("post/view"))
            .andExpect(model().attributeExists("post"));
    }
}
