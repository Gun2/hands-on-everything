package com.example.board.service;

import com.example.board.entity.Post;
import com.example.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void createPost_ShouldSaveAndReturnPost() {
        Post post = Post.builder().title("Test").build();
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(post);

        Post created = postService.createPost(post);
        assertNotNull(created);
        assertEquals("Test", created.getTitle());
    }

    @Test
    public void getPost_ShouldReturnPost_WhenExists() {
        Post post = Post.builder().id(1L).title("Test").build();
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post found = postService.getPost(1L);
        assertEquals(1L, found.getId());
    }
}
