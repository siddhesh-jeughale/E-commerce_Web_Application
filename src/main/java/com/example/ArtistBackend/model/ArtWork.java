package com.example.ArtistBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "artworks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // "Oil Painting", "Watercolor", "Acrylic"
    @Column(nullable = false)
    private String medium;

    // "Original", "Print"
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String size;

    @CreationTimestamp
    private LocalDateTime createdDate = LocalDateTime.now();
    public Integer getYear() {
        return createdDate != null ? createdDate.getYear() : null;
    }

    @Column(nullable = false)
    private String imgUrl;



}
