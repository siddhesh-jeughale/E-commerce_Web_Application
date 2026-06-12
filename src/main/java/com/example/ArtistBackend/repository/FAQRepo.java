package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQRepo extends JpaRepository<FAQ,Long> {
}
