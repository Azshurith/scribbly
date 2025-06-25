package com.devitrax.scribbly.repository;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for accessing and managing {@link Post} entities.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Finds all posts authored by the given user.
     *
     * @param user the user whose posts to retrieve
     * @return list of posts authored by the user
     */
    List<Post> findByUser(User user);
}
