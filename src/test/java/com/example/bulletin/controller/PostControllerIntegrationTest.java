package com.example.bulletin.controller;

import com.example.bulletin.entity.Post;
import com.example.bulletin.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("create post success")
    void createPostSuccess() throws Exception {
        String request = """
                {
                  "title": "First Post",
                  "content": "Hello bulletin board",
                  "author": "alice"
                }
                """;

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").value("First Post"));
    }

    @Test
    @DisplayName("create post validation failure")
    void createPostValidationFailure() throws Exception {
        String request = """
                {
                  "title": "",
                  "content": "content",
                  "author": "alice"
                }
                """;

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("get post list with pagination success")
    void getPostListSuccess() throws Exception {
        postRepository.save(new Post("title1", "content1", "author1"));
        postRepository.save(new Post("title2", "content2", "author2"));

        mockMvc.perform(get("/api/posts?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    @DisplayName("get post detail success")
    void getPostDetailSuccess() throws Exception {
        Post post = postRepository.save(new Post("detail", "content", "author"));

        mockMvc.perform(get("/api/posts/{id}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(post.getId()))
                .andExpect(jsonPath("$.data.title").value("detail"));
    }

    @Test
    @DisplayName("get post detail not found")
    void getPostDetailNotFound() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("POST_NOT_FOUND"));
    }

    @Test
    @DisplayName("update post success")
    void updatePostSuccess() throws Exception {
        Post post = postRepository.save(new Post("old", "old content", "old-author"));

        String request = """
                {
                  "title": "new",
                  "content": "new content",
                  "author": "new-author"
                }
                """;

        mockMvc.perform(put("/api/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("new"))
                .andExpect(jsonPath("$.data.author").value("new-author"));
    }

    @Test
    @DisplayName("update post not found")
    void updatePostNotFound() throws Exception {
        String request = """
                {
                  "title": "new",
                  "content": "new content",
                  "author": "new-author"
                }
                """;

        mockMvc.perform(put("/api/posts/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("POST_NOT_FOUND"));
    }

    @Test
    @DisplayName("delete post success")
    void deletePostSuccess() throws Exception {
        Post post = postRepository.save(new Post("delete", "content", "author"));

        mockMvc.perform(delete("/api/posts/{id}", post.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("delete post not found")
    void deletePostNotFound() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("POST_NOT_FOUND"));
    }
}
