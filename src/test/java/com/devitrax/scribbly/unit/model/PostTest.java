package com.devitrax.scribbly.unit.model;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void shouldSetAndGetFields() {
        User author = new User();
        author.setUsername("scribbler");

        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Title");
        post.setContent("This is a test content.");
        post.setCreatedAt(now);
        post.setUser(author);

        assertThat(post.getId()).isEqualTo(1L);
        assertThat(post.getTitle()).isEqualTo("Test Title");
        assertThat(post.getContent()).isEqualTo("This is a test content.");
        assertThat(post.getCreatedAt()).isEqualTo(now);
        assertThat(post.getUser()).isEqualTo(author);
    }

    @Test
    void shouldSetAndGetUser() {
        User user = new User();
        user.setUsername("author");

        Post post = new Post();
        post.setUser(user);

        assertThat(post.getUser().getUsername()).isEqualTo("author");
    }
}
