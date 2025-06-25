package com.devitrax.scribbly.service;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.repository.PostRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for handling operations related to {@link Post}.
 * Includes caching for performance optimization.
 */
@Service
public class PostService {

    /** Repository for accessing post data. */
    private final PostRepository postRepo;

    /**
     * Constructs a PostService with the given repository.
     *
     * @param postRepo the post repository
     */
    public PostService(PostRepository postRepo) {
        this.postRepo = postRepo;
    }

    /**
     * Retrieves a post by its ID, with caching support.
     *
     * @param id the ID of the post
     * @return the post with the given ID
     * @throws RuntimeException if the post is not found
     */
    @Cacheable(value = "posts", key = "#id")
    public Post getPostById(Long id) {
        return postRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Post with ID " + id + " not found"));
    }

    /**
     * Saves a new or existing post.
     * Updates the cache for the specific post and clears cached pages.
     *
     * @param post the post to save
     * @return the saved post
     */
    @CachePut(value = "posts", key = "#post.id")
    @CacheEvict(value = "postsPage", allEntries = true)
    public Post save(Post post) {
        return postRepo.save(post);
    }

    /**
     * Deletes a post by its ID.
     * Evicts the cache entry for the specific post and clears all cached pages.
     *
     * @param id the ID of the post to delete
     */
    @Caching(evict = {
        @CacheEvict(value = "posts", key = "#id"),
        @CacheEvict(value = "postsPage", allEntries = true)
    })
    public void deleteById(Long id) {
        postRepo.deleteById(id);
    }

    /**
     * Retrieves a paginated list of posts, with caching support per page number.
     *
     * @param pageable the pagination and sorting information
     * @return a page of posts
     */
    @Cacheable(value = "postsPage", key = "#pageable.pageNumber")
    public Page<Post> getByPage(Pageable pageable) {
        return postRepo.findAll(pageable);
    }
}
