package com.devitrax.scribbly.integration.repository;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.PostRepository;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("repoTester");
        testUser.setPassword("secure");
        testUser = userRepository.save(testUser);
    }

    @Test
    void shouldSaveAndFindPostByUser() {
        Post post1 = new Post();
        post1.setTitle("First Post");
        post1.setContent("Content A");
        post1.setUser(testUser);

        Post post2 = new Post();
        post2.setTitle("Second Post");
        post2.setContent("Content B");
        post2.setUser(testUser);

        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> userPosts = postRepository.findByUser(testUser);

        assertThat(userPosts).hasSize(2);
        assertThat(userPosts).extracting("title").containsExactlyInAnyOrder("First Post", "Second Post");
    }

    @Test
    void shouldDeletePost() {
        Post post = new Post();
        post.setTitle("Temp Post");
        post.setContent("Temporary content");
        post.setUser(testUser);
        post = postRepository.save(post);

        postRepository.delete(post);

        List<Post> remaining = postRepository.findByUser(testUser);
        assertThat(remaining).isEmpty();
    }
}
