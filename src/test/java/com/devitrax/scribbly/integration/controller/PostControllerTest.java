package com.devitrax.scribbly.integration.controller;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.PostRepository;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User testUser;
    private Post testPost;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        testPost = new Post();
        testPost.setTitle("Integration Title");
        testPost.setContent("Integration Content");
        testPost.setUser(testUser);
        testPost = postRepository.save(testPost);
    }

    @Test
    void shouldDisplayPostList() throws Exception {
        mockMvc.perform(get("/post/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("post/list"))
            .andExpect(model().attributeExists("posts"));
    }

    @Test
    void shouldDisplaySinglePostView() throws Exception {
        mockMvc.perform(get("/post/" + testPost.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("post/view"))
            .andExpect(model().attributeExists("post"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldAccessNewPostForm() throws Exception {
        mockMvc.perform(get("/post/form/new"))
            .andExpect(status().isOk())
            .andExpect(view().name("post/form"))
            .andExpect(model().attributeExists("post"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldCreateNewPost() throws Exception {
        mockMvc.perform(post("/post/create")
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("title", "New Post")
            .param("content", "Post content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldUpdateExistingPost() throws Exception {
        mockMvc.perform(post("/post/update/" + testPost.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("title", "Updated")
            .param("content", "Updated content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + testPost.getId()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldDeleteOwnedPost() throws Exception {
        mockMvc.perform(post("/post/delete/" + testPost.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/list"));
    }
}
