package com.ContentAPI.ContentAPI.repositories;

import com.ContentAPI.ContentAPI.domain.Content;
import com.ContentAPI.ContentAPI.domain.Rating;
import com.ContentAPI.ContentAPI.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByUser(User user);
    List<Rating> findByContentId(Long contentId);

}
