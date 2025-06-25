package com.devitrax.scribbly.unit.service;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.repository.PostRepository;
import com.devitrax.scribbly.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepo;

    @InjectMocks
    private PostService postService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPostById_shouldReturnPost_whenPostExists() {
        Post post = new Post();
        post.setId(1L);
        when(postRepo.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.getPostById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getPostById_shouldThrow_whenPostNotFound() {
        when(postRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> postService.getPostById(999L));
    }

    @Test
    void save_shouldCallRepoSave() {
        Post post = new Post();
        post.setTitle("Test");
        when(postRepo.save(post)).thenReturn(post);

        Post saved = postService.save(post);
        assertEquals("Test", saved.getTitle());
    }

    @Test
    void deleteById_shouldCallRepoDelete() {
        postService.deleteById(1L);
        verify(postRepo, times(1)).deleteById(1L);
    }

    @Test
    void getByPage_shouldReturnPagedPosts() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Post> page = new PageImpl<>(Collections.emptyList());
        when(postRepo.findAll(pageable)).thenReturn(page);

        Page<Post> result = postService.getByPage(pageable);
        assertEquals(0, result.getTotalElements());
    }
}
