package com.ContentAPI.ContentAPI.dtos;

import com.ContentAPI.ContentAPI.domain.RoleName;

public record RegisterRequestDTO(String login,
                                 String password,

                                 RoleName role
                              ) {
}