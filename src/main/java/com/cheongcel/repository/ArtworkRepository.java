package com.cheongcel.repository;

import com.cheongcel.domain.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findByPublishedTrueOrderByCreatedAtDesc();
    List<Artwork> findByCategoryAndPublishedTrue(String category);
}
