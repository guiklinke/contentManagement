package com.ContentAPI.ContentAPI.dtos;

import org.hibernate.boot.archive.scan.spi.ClassDescriptor;

import java.time.LocalDateTime;

public record RatingResponseDTO(int score,
                                String comment,
                                LocalDateTime createdAt,
                                String userLogin,
                                String contentTitle

                                ) {
}
