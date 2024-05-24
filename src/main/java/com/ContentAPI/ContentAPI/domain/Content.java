package com.ContentAPI.ContentAPI.domain;


import com.ContentAPI.ContentAPI.dtos.ContentRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "content")
@Entity(name = "content")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 7, nullable = false)
    private Category category;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "content_url", nullable = false)
    private String contentUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "content")
    private List<Rating> ratings = new ArrayList<>();

    public Content(ContentRequestDTO contentDTO) {
        this.title = contentDTO.title();
        this.description = contentDTO.description();
        this.category = contentDTO.category();
        this.thumbnailUrl = contentDTO.thumbnailUrl();
        this.contentUrl = contentDTO.contentUrl();
    }
}



