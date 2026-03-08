package com.example.bulletin.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long postId) {
        super("Post not found. id=" + postId);
    }
}
