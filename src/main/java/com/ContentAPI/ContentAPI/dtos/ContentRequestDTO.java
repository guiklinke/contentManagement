package com.ContentAPI.ContentAPI.dtos;

import com.ContentAPI.ContentAPI.domain.Category;

public record ContentRequestDTO(String title,
                         String description,
                         Category category,
                         String thumbnailUrl,
                         String contentUrl
                         ) {
}
