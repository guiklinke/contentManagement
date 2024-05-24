package com.ContentAPI.ContentAPI.services;

import com.ContentAPI.ContentAPI.domain.Content;
import com.ContentAPI.ContentAPI.dtos.ContentRequestDTO;
import com.ContentAPI.ContentAPI.exceptions.InvalidContentException;
import com.ContentAPI.ContentAPI.exceptions.ResourceNotFoundException;
import com.ContentAPI.ContentAPI.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;

    public Content saveContent(ContentRequestDTO content) {
        if (content == null) {
            throw new InvalidContentException("Content request cannot be null");
        }

        Content newContent = new Content(content);
        return contentRepository.save(newContent);
    }

    public Content updateContent(Long id, ContentRequestDTO content) {
        Content existingContent = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found with id: " + id));

        existingContent.setTitle(content.title());
        existingContent.setDescription(content.description());
        existingContent.setCategory(content.category());
        existingContent.setThumbnailUrl(content.thumbnailUrl());
        existingContent.setContentUrl(content.contentUrl());

        return contentRepository.save(existingContent);
    }

    public List<Content> findAll() {
        List<Content> allContent = contentRepository.findAll();
        if (allContent.isEmpty()) {
            throw new ResourceNotFoundException("No content found in the database");
        }
        return allContent;
    }

    public Content findById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found with id: " + id));
    }

    public void deleteContent(Long id) {
        if (!contentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Content not found with id: " + id);
        }
        contentRepository.deleteById(id);
    }
}
