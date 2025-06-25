package com.devitrax.scribbly.integration.service;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.PostRepository;
import com.devitrax.scribbly.repository.UserRepository;
import com.devitrax.scribbly.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CacheManager cacheManager;

    private User user;
    private Post post;

    @BeforeEach
    void setup() {
        postRepo.deleteAll();
        userRepo.deleteAll();

        user = new User();
        user.setUsername("testuser");
        user.setPassword("testpass");
        user = userRepo.save(user);

        post = new Post();
        post.setTitle("Sample");
        post.setContent("Content");
        post.setUser(user);
        post = postService.save(post);
    }

    @Test
    void shouldSavePostAndUpdateCache() {
        Post cached = cacheManager.getCache("posts").get(post.getId(), Post.class);
        assertThat(cached).isNotNull();
        assertThat(cached.getTitle()).isEqualTo("Sample");
    }

    @Test
    void shouldGetPostByIdWithCache() {
        Post fetched = postService.getPostById(post.getId());
        assertThat(fetched.getTitle()).isEqualTo("Sample");
    }

    @Test
    void shouldGetPaginatedPostsAndCachePage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> page = postService.getByPage(pageable);
        assertThat(page.getContent()).hasSize(1);

        Page<Post> cachedPage = cacheManager.getCache("postsPage").get(0, Page.class);
        assertThat(cachedPage).isNotNull(); // Cached version available
    }

    @Test
    void shouldDeletePostAndEvictCaches() {
        postService.deleteById(post.getId());

        assertThat(postRepo.findById(post.getId())).isEmpty();
        assertThat(cacheManager.getCache("posts").get(post.getId())).isNull();
        assertThat(cacheManager.getCache("postsPage").get(0)).isNull(); // allEntries = true
    }
}
