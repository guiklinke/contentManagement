package com.ContentAPI.ContentAPI.dtos;

import com.ContentAPI.ContentAPI.domain.Category;

import java.time.LocalDateTime;

public record ContentResponseDTO(
        String title,
        String description,
        Category category,
        String thumbnailUrl,
        String contentUrl,
        LocalDateTime createdAt
) {}

