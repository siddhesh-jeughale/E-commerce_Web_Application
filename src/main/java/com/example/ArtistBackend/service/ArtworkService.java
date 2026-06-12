package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.ArtWork;
import com.example.ArtistBackend.repository.ArtworkRepo;
import com.example.ArtistBackend.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ArtworkService {

    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private ArtworkRepo artworkRepo;

    public ArtWork saveArtwork(ArtWork artwork, MultipartFile imageFile) throws IOException {

        if (imageFile != null && !imageFile.isEmpty()) {
//            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            String fileName = FileUploadUtil.generateFileName(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            artwork.setImgUrl(fileName);

        }
        return artworkRepo.save(artwork);
    }

    public List<ArtWork> getAllArtworks() throws IOException {
        return artworkRepo.findAll();
    }
    public void  delete(Long id)  {
        artworkRepo.deleteById(id);
    }

    public ArtWork getArtworkById(Long id) {
        return artworkRepo.findById(id).orElseThrow(() -> new RuntimeException("Artwork not found"));
    }

    public ArtWork updateArtwork(ArtWork artwork, MultipartFile imageFile) throws IOException {
        ArtWork existing = artworkRepo.findById(artwork.getId())
                .orElseThrow(() -> new RuntimeException("Artwork not found"));

        // Update fields — do NOT touch createdDate (@CreationTimestamp is Hibernate-managed)
        existing.setTitle(artwork.getTitle());
        existing.setMedium(artwork.getMedium());
        existing.setCategory(artwork.getCategory());
        existing.setPrice(artwork.getPrice());
        existing.setStatus(artwork.getStatus());
        existing.setSize(artwork.getSize());


        if (imageFile != null && !imageFile.isEmpty()) {

//            1. Delete old image
            String oldImgUrl = existing.getImgUrl();
            if (oldImgUrl != null && !oldImgUrl.isBlank()) {
//                String oldFileName = oldImgUrl.replace("/images/", "");
                Path oldPath = Paths.get(uploadDir).resolve(oldImgUrl);
                Files.deleteIfExists(oldPath);
            }

//            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
//            String fileName = imageFile.getOriginalFilename();
            String fileName = FileUploadUtil.generateFileName(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            existing.setImgUrl(fileName);
        }

        return artworkRepo.save(existing);
    }

//    <-----------------------------------for loadMore artWork----------------------------------->
public Page<ArtWork> getArtworksPage(int page, int size) {
    return artworkRepo.findAll(
            PageRequest.of(page, size, Sort.by("createdDate").descending())
    );
}

}
