package com.ContentAPI.ContentAPI.domain;


import com.ContentAPI.ContentAPI.dtos.RatingRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "rating")
@Entity(name = "rating")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Min(1)
    @Max(5)
    @Column(name = "score", nullable = false)
    private int score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    public Rating(Content content, User user, RatingRequestDTO ratingRequestDTO) {
        this.user = user;
        this.content = content;
        this.score = ratingRequestDTO.score();
        this.comment = ratingRequestDTO.comment();
    }
}
