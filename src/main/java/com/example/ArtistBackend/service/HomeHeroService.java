package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.HomeHero;
import com.example.ArtistBackend.repository.HomeHeroRepo;
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
public class HomeHeroService {
    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private HomeHeroRepo homeHeroRepo;

    public void SaveChanges (HomeHero homeHero, MultipartFile imageFile) throws IOException {
        System.out.println("service homme hero heading: " + homeHero.getHeading());
        System.out.println("service homme hero subtext: " + homeHero.getSubtext());
        if (imageFile != null) System.out.println("service homme hero image: " + imageFile.getOriginalFilename());
        if (imageFile != null && !imageFile.isEmpty()){
//            String  fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            String fileName = FileUploadUtil.generateFileName(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);
               homeHero.setImageUrl(fileName);

        }
        homeHeroRepo.save(homeHero);
    }

    public void deleteHero (Long id) throws IOException {
        homeHeroRepo.deleteById(id);
    }

    public List<HomeHero> getAllHeroes() throws IOException {
        return homeHeroRepo.findAll();
    }
    public HomeHero getById (Long id) throws IOException {
       return homeHeroRepo.findById(id).orElseThrow(() -> new RuntimeException(" not found"));
    }

    public HomeHero updateHero(HomeHero homeHero, MultipartFile imageFile) throws IOException {
        HomeHero existing = homeHeroRepo.findById(homeHero.getId())
                .orElseThrow(() -> new RuntimeException("HeroSlide not found"));
        // Update fields
        existing.setHeading(homeHero.getHeading());
        existing.setSubtext(homeHero.getSubtext());

        System.out.println("new update  item:" +  homeHero.getHeading());
        System.out.println("new update image:" +  homeHero.getImageUrl());
        System.out.println("new update image:" +  homeHero.getSubtext());

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


        return homeHeroRepo.save(existing);
    }
}
