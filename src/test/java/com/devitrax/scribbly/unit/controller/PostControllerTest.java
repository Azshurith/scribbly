package com.devitrax.scribbly.unit.controller;

import com.devitrax.scribbly.controller.PostController;
import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import com.devitrax.scribbly.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PostControllerTest {

    private PostService postService;
    private UserRepository userRepo;
    private PostController controller;

    @BeforeEach
    void setup() {
        postService = mock(PostService.class);
        userRepo = mock(UserRepository.class);
        controller = new PostController(postService, userRepo);
    }

    @Test
    void shouldDisplayPostList() {
        Model model = mock(Model.class);
        Page<Post> page = new PageImpl<>(List.of(new Post()));
        when(postService.getByPage(any())).thenReturn(page);

        String result = controller.list(0, 5, model);

        assertThat(result).isEqualTo("post/list");
        verify(model).addAttribute(eq("posts"), any());
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("totalPages", page.getTotalPages());
    }

    @Test
    void shouldDisplayNewPostForm() {
        Model model = mock(Model.class);
        String result = controller.form("new", model, mock(Principal.class));

        assertThat(result).isEqualTo("post/form");
        verify(model).addAttribute(eq("post"), any(Post.class));
    }

    @Test
    void shouldRedirectOnInvalidPostId() {
        String result = controller.form("abc", mock(Model.class), mock(Principal.class));
        assertThat(result).isEqualTo("redirect:/post/list");
    }

    @Test
    void shouldRedirectIfUserMismatchOnEdit() {
        Post post = new Post();
        User user = new User();
        user.setUsername("owner");
        post.setUser(user);

        when(postService.getPostById(1L)).thenReturn(post);

        Principal principal = () -> "hacker";
        String result = controller.form("1", mock(Model.class), principal);

        assertThat(result).isEqualTo("redirect:/post/list");
    }

    @Test
    void shouldReturnFormIfUserOwnsPost() {
        Post post = new Post();
        User user = new User();
        user.setUsername("owner");
        post.setUser(user);

        when(postService.getPostById(1L)).thenReturn(post);
        Model model = mock(Model.class);
        Principal principal = () -> "owner";

        String result = controller.form("1", model, principal);

        assertThat(result).isEqualTo("post/form");
        verify(model).addAttribute("post", post);
    }

    @Test
    void shouldCreatePost() {
        User user = new User();
        user.setUsername("john");

        Post post = new Post();
        Principal principal = () -> "john";

        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));

        String result = controller.create(post, principal);

        assertThat(result).isEqualTo("redirect:/post/list");
        verify(postService).save(post);
        assertThat(post.getUser()).isEqualTo(user);
    }

    @Test
    void shouldUpdateOwnedPost() {
        Post existingPost = new Post();
        existingPost.setId(1L); // <-- Add this line
        User user = new User();
        user.setUsername("john");
        existingPost.setUser(user);

        Post updated = new Post();
        updated.setTitle("New");
        updated.setContent("Updated");

        Principal principal = () -> "john";

        when(postService.getPostById(1L)).thenReturn(existingPost);

        String result = controller.update(1L, updated, principal);

        assertThat(result).isEqualTo("redirect:/post/1");
        verify(postService).save(existingPost);
        assertThat(existingPost.getTitle()).isEqualTo("New");
        assertThat(existingPost.getContent()).isEqualTo("Updated");
    }

    @Test
    void shouldNotUpdateIfNotOwner() {
        Post post = new Post();
        User user = new User();
        user.setUsername("realowner");
        post.setUser(user);

        when(postService.getPostById(1L)).thenReturn(post);

        Principal principal = () -> "imposter";
        String result = controller.update(1L, new Post(), principal);

        assertThat(result).isEqualTo("redirect:/post/list");
    }

    @Test
    void shouldDeleteIfOwner() {
        Post post = new Post();
        User user = new User();
        user.setUsername("john");
        post.setUser(user);

        when(postService.getPostById(1L)).thenReturn(post);
        Principal principal = () -> "john";

        String result = controller.delete(1L, principal);

        assertThat(result).isEqualTo("redirect:/post/list");
        verify(postService).deleteById(1L);
    }

    @Test
    void shouldNotDeleteIfNotOwner() {
        Post post = new Post();
        User user = new User();
        user.setUsername("owner");
        post.setUser(user);

        when(postService.getPostById(1L)).thenReturn(post);
        Principal principal = () -> "notowner";

        String result = controller.delete(1L, principal);

        assertThat(result).isEqualTo("redirect:/post/list");
        verify(postService, never()).deleteById(1L);
    }
}
