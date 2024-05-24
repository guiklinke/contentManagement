package com.ContentAPI.ContentAPI.dtos;


import java.time.LocalDateTime;


public record RegisterResponseDTO(
    String login,

    LocalDateTime createdAt

)


 {
}
