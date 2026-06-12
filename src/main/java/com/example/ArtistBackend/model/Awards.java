package com.example.ArtistBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Awards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Awards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subtitle;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    // User-specified award date (e.g. when the award was received)
    private LocalDateTime awardDate;

    // Auto-managed by Hibernate — when this DB record was created
    @CreationTimestamp
    private LocalDateTime createdDate = LocalDateTime.now();
    public Integer getYear() {
        return createdDate != null ? createdDate.getYear() : null;
    }
}
