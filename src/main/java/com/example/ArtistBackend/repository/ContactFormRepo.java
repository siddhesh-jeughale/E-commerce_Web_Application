package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactFormRepo extends JpaRepository<Contact, Long> {
}
