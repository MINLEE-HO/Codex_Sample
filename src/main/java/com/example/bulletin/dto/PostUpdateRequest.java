package com.example.bulletin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @NotBlank(message = "title is required")
        @Size(max = 100, message = "title must be at most 100 characters")
        String title,

        @NotBlank(message = "content is required")
        String content,

        @NotBlank(message = "author is required")
        @Size(max = 50, message = "author must be at most 50 characters")
        String author
) {
}
