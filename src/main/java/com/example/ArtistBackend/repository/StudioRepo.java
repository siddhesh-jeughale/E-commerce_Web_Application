package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioRepo extends JpaRepository<Studio,Long> {

}
