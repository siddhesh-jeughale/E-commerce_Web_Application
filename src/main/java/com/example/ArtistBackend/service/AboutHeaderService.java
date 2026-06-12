package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.AboutHero;
import com.example.ArtistBackend.model.Testimonial;
import com.example.ArtistBackend.repository.AboutRepo;
import com.example.ArtistBackend.repository.TestimonialRepo;
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
public class AboutHeaderService {
    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private AboutRepo aboutRepo;

    public void saveHeader(AboutHero aboutHero,MultipartFile imageFile) throws IOException {
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
            aboutHero.setImageUrl(fileName);
        }
        aboutRepo.save(aboutHero);
    }

    public void DeleteHeader(Long id) {
        aboutRepo.deleteById(id);
    }

    public List<AboutHero> findAllHeader() {
        return aboutRepo.findAll();
    }
    public AboutHero findHeaderById(Long id) {
        return aboutRepo.findById(id).
                orElseThrow(() -> new RuntimeException("Header with id " + id + " not found"));
    }

    public AboutHero updateHeader(AboutHero aboutHero, MultipartFile imageFile) throws IOException {
        AboutHero existing = aboutRepo.findById(aboutHero.getId())
                .orElseThrow(() -> new RuntimeException("Header not found with id: " + aboutHero.getId()));
        System.out.println("Received ID: " + aboutHero.getId());

        existing.setTitle(aboutHero.getTitle());
        existing.setSubtext(aboutHero.getSubtext());
        System.out.println("in about service image"+ imageFile );

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
        return aboutRepo.save(existing);
    }

}
