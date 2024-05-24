package com.ContentAPI.ContentAPI.services;

import com.ContentAPI.ContentAPI.domain.Content;
import com.ContentAPI.ContentAPI.domain.Rating;
import com.ContentAPI.ContentAPI.domain.User;
import com.ContentAPI.ContentAPI.dtos.RatingRequestDTO;
import com.ContentAPI.ContentAPI.exceptions.ResourceNotFoundException;
import com.ContentAPI.ContentAPI.repositories.ContentRepository;
import com.ContentAPI.ContentAPI.repositories.RatingRepository;
import com.ContentAPI.ContentAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    public Rating createRating(Long userId, Long contentId, RatingRequestDTO ratingRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found with id: " + contentId));

        try {
            Rating newRating = new Rating(content, user, ratingRequestDTO);
            saveRating(newRating);
            return newRating;
        } catch (Exception e) {
            throw new RuntimeException("Error creating rating: " + e.getMessage());
        }
    }

    public Rating updateRating(Long id, Rating updatedRating) {
        Optional<Rating> optionalRating = ratingRepository.findById(id);

        if (optionalRating.isPresent()) {
            Rating existingRating = optionalRating.get();
            existingRating.setScore(updatedRating.getScore());
            existingRating.setComment(updatedRating.getComment());
            return ratingRepository.save(existingRating);
        } else {
            throw new ResourceNotFoundException("Rating not found with id: " + id);
        }
    }

    public List<Rating> getRatingsByContent(Long contentId) {
        List<Rating> ratings = ratingRepository.findByContentId(contentId);
        if (ratings.isEmpty()) {
            throw new ResourceNotFoundException("No ratings found for content with id: " + contentId);
        }
        return ratings;
    }

    public double getAverageRating(Long contentId) {
        List<Rating> ratings = ratingRepository.findByContentId(contentId);
        if (ratings.isEmpty()) {
            throw new ResourceNotFoundException("No ratings found for content with id: " + contentId);
        }
        return ratings.stream().mapToInt(Rating::getScore).average().orElse(0.0);
    }

    public void saveRating(Rating rating) {
        try {
            ratingRepository.save(rating);
        } catch (Exception e) {
            throw new RuntimeException("Error saving rating: " + e.getMessage());
        }
    }
    public void deleteRating(Long id) {
        if (!ratingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rating not found with id: " + id);
        }
        ratingRepository.deleteById(id);
    }
}
