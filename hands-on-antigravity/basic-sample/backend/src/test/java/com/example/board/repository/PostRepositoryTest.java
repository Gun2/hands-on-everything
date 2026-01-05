package com.example.board.repository;

import com.example.board.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void saveAndFindAll_ShouldReturnPosts() {
        Post post = Post.builder().title("Title").content("Content").author("User").build();
        postRepository.save(post);

        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("Title");
        assertThat(posts.get(0).getCreatedAt()).isNotNull();
    }
}
