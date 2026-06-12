package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.ContactHero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepo extends JpaRepository<ContactHero, Long> {
}
