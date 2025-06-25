package com.devitrax.scribbly.integration.model;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.PostRepository;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setUsername("postTester");
        user.setPassword("secure123");
        user = userRepository.save(user);
    }

    @Test
    void shouldCreateAndRetrievePost() {
        Post post = new Post();
        post.setTitle("Integration Title");
        post.setContent("Integration Content");
        post.setUser(user);

        Post saved = postRepository.save(post);

        Optional<Post> retrieved = postRepository.findById(saved.getId());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getTitle()).isEqualTo("Integration Title");
        assertThat(retrieved.get().getUser().getUsername()).isEqualTo("postTester");
    }

    @Test
    void shouldUpdatePost() {
        Post post = new Post();
        post.setTitle("Original Title");
        post.setContent("Original Content");
        post.setUser(user);
        post = postRepository.save(post);

        post.setTitle("Updated Title");
        post.setContent("Updated Content");
        post = postRepository.save(post);

        Post updated = postRepository.findById(post.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("Updated Title");
        assertThat(updated.getContent()).isEqualTo("Updated Content");
    }

    @Test
    void shouldDeletePost() {
        Post post = new Post();
        post.setTitle("To Be Deleted");
        post.setContent("Delete me");
        post.setUser(user);
        post = postRepository.save(post);

        postRepository.deleteById(post.getId());

        Optional<Post> deleted = postRepository.findById(post.getId());
        assertThat(deleted).isEmpty();
    }
}
