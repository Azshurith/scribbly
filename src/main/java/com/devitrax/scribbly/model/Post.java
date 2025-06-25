package com.devitrax.scribbly.model;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;

/**
 * Entity representing a blog post in the Scribbly application.
 */
@Entity
public class Post {

    /** The unique identifier of the post. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The title of the post. */
    private String title;

    /** The main content of the post. */
    @Column(length = 5000)
    private String content;

    /** The timestamp when the post was created. */
    private LocalDateTime createdAt = LocalDateTime.now();

    /** The user who authored the post. */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters

    /**
     * Gets the post ID.
     * @return the post ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the post ID.
     * @param id the ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the post title.
     * @return the post title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the post title.
     * @param title the title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the post content.
     * @return the post content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the post content.
     * @param content the content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the post creation timestamp.
     * @return the timestamp of creation.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the post creation timestamp.
     * @param createdAt the timestamp to set.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the user who authored the post.
     * @return the author of the post.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who authored the post.
     * @param user the user to set.
     */
    public void setUser(User user) {
        this.user = user;
    }
}