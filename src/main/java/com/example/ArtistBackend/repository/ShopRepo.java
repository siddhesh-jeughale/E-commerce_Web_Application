package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.ShopHero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepo extends JpaRepository<ShopHero, Long> {
}
