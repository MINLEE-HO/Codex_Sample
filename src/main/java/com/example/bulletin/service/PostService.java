package com.example.bulletin.service;

import com.example.bulletin.dto.PostCreateRequest;
import com.example.bulletin.dto.PostResponse;
import com.example.bulletin.dto.PostUpdateRequest;
import com.example.bulletin.entity.Post;
import com.example.bulletin.exception.PostNotFoundException;
import com.example.bulletin.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse create(PostCreateRequest request) {
        Post post = new Post(request.title(), request.content(), request.author());
        return PostResponse.from(postRepository.save(post));
    }

    public Page<PostResponse> getList(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostResponse::from);
    }

    public PostResponse getById(Long id) {
        return PostResponse.from(findPost(id));
    }

    @Transactional
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = findPost(id);
        post.update(request.title(), request.content(), request.author());
        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = findPost(id);
        postRepository.delete(post);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }
}
