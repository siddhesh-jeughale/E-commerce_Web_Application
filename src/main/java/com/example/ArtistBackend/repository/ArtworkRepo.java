package com.example.ArtistBackend.repository;

import com.example.ArtistBackend.model.ArtWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkRepo extends JpaRepository<ArtWork, Long> {
    // SHOP FILTERS
//    List<ArtWork> findByCategoryAndInStock(String category, Boolean inStock);
//    List<ArtWork> findByCategoryOrderByPriceAsc(String category);
//    List<ArtWork> findByInStockTrue();

    // GALLERY FILTERS
//    List<ArtWork> findByMedium(String medium);
//    List<ArtWork> findByMediumOrderByCreatedDateDesc(String medium);

    // COMBINED FILTERS
//    List<ArtWork> findByCategoryAndMedium(String category, String medium);
}
