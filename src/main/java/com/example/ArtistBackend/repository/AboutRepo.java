package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.AboutHero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutRepo extends JpaRepository<AboutHero, Long> {

}
