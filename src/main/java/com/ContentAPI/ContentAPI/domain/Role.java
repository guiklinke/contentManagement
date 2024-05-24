package com.ContentAPI.ContentAPI.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "roles")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

}