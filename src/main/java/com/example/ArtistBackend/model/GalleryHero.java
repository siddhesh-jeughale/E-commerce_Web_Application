package com.example.ArtistBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "GalleryHeader")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GalleryHero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subtext;

    @Column(nullable = false)
    private String imageUrl;
}
