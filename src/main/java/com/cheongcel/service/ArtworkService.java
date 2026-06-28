package com.cheongcel.service;

import com.cheongcel.domain.Artwork;
import com.cheongcel.repository.ArtworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtworkService {

    private final ArtworkRepository artworkRepository;

    public List<Artwork> getArtworks(String category) {
        if (category != null && !category.isBlank()) {
            return artworkRepository.findByCategoryAndPublishedTrue(category);
        }
        return artworkRepository.findByPublishedTrueOrderByCreatedAtDesc();
    }

    public List<Artwork> getRecentArtworks(int limit) {
        return artworkRepository.findByPublishedTrueOrderByCreatedAtDesc()
                .stream().limit(limit).toList();
    }

    public Artwork getArtwork(Long id) {
        return artworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artwork not found: " + id));
    }

    @Transactional
    public Artwork save(Artwork artwork) {
        return artworkRepository.save(artwork);
    }

    @Transactional
    public void delete(Long id) {
        artworkRepository.deleteById(id);
    }
}
