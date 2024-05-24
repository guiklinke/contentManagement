package com.ContentAPI.ContentAPI.repositories;

import com.ContentAPI.ContentAPI.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {

}
