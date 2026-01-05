package com.example.board.controller;

import com.example.board.entity.Post;
import com.example.board.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllPosts_ShouldReturnList() throws Exception {
        Post post1 = Post.builder().id(1L).title("Title 1").build();
        Mockito.when(postService.getAllPosts()).thenReturn(Arrays.asList(post1));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title 1"));
    }

    @Test
    public void createPost_ShouldReturnCreatedPost() throws Exception {
        Post post = Post.builder().title("New Post").content("Content").author("Author").build();
        Post savedPost = Post.builder().id(1L).title("New Post").content("Content").author("Author").build();

        Mockito.when(postService.createPost(any(Post.class))).thenReturn(savedPost);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Post"));
    }
}
