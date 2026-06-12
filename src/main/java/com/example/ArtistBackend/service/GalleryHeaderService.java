package com.example.ArtistBackend.service;


import com.example.ArtistBackend.model.GalleryHero;
import com.example.ArtistBackend.repository.GalleryRepo;
import com.example.ArtistBackend.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class GalleryHeaderService {
    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private GalleryRepo galleryRepo;

    public void saveGalleryHeader(GalleryHero galleryHero, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()){
//            String  fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            String fileName = FileUploadUtil.generateFileName(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            Files.copy(imageFile.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            galleryHero.setImageUrl(fileName);
        }
        galleryRepo.save(galleryHero);
    }

    public void DeleteGalleryHeader(Long id) {
        galleryRepo.deleteById(id);
    }

    public List<GalleryHero> findAllGalleryHeader() {
        return galleryRepo.findAll();
    }
    public GalleryHero findGalleryHeaderById(Long id) {
        return galleryRepo.findById(id).
                orElseThrow(() -> new RuntimeException("Header with id " + id + " not found"));
    }

    public GalleryHero updateGalleryHeader(GalleryHero galleryHero, MultipartFile imageFile) throws IOException {
        GalleryHero existing = galleryRepo.findById(galleryHero.getId())
                .orElseThrow(() -> new RuntimeException("Gallery header not found with id: " + galleryHero.getId()));

        existing.setTitle(galleryHero.getTitle());
        existing.setSubtext(galleryHero.getSubtext());

        if (imageFile != null && !imageFile.isEmpty()) {
            String oldImgUrl = existing.getImageUrl();
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
            existing.setImageUrl(fileName);
        }
        return galleryRepo.save(existing);
    }

}
