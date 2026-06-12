package com.example.ArtistBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Testimonial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Testimonial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Must be wrapper Long, not primitive long, for JPA @Id

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String quote;

    @Column(nullable = false)
    private String imageUrl;
}
