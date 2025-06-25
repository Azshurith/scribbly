package com.devitrax.scribbly.unit.repository;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.PostRepository;
import com.devitrax.scribbly.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    @Test
    void shouldFindPostsByUser() {
        // Arrange
        User user = new User();
        user.setUsername("author1");
        user.setPassword("pass");
        userRepo.save(user);

        Post post1 = new Post();
        post1.setTitle("First Post");
        post1.setContent("Hello World");
        post1.setUser(user);

        Post post2 = new Post();
        post2.setTitle("Second Post");
        post2.setContent("Another one");
        post2.setUser(user);

        postRepo.save(post1);
        postRepo.save(post2);

        // Act
        List<Post> posts = postRepo.findByUser(user);

        // Assert
        assertThat(posts).hasSize(2);
        assertThat(posts).extracting("title")
                .containsExactlyInAnyOrder("First Post", "Second Post");
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoPosts() {
        // Arrange
        User user = new User();
        user.setUsername("emptyUser");
        user.setPassword("none");
        userRepo.save(user);

        // Act
        List<Post> posts = postRepo.findByUser(user);

        // Assert
        assertThat(posts).isEmpty();
    }
}
