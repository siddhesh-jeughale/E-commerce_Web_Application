package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.HomeHero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeHeroRepo extends JpaRepository<HomeHero, Long> {
}
