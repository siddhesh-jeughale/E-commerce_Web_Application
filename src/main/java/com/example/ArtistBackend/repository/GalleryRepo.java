package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.GalleryHero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepo extends JpaRepository<GalleryHero, Long> {
}
