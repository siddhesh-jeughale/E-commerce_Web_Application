package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.Awards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardRepo extends JpaRepository<Awards,Long> {
}
