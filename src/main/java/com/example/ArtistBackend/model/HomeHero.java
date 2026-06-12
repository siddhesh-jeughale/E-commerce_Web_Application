package com.example.ArtistBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "home_hero")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeHero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String heading;

    @Column(nullable = false)
    private String subtext;

    @Column(nullable = false)
    private String imageUrl;


}
