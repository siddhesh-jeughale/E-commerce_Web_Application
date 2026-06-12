package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.ShopHero;
import com.example.ArtistBackend.repository.ShopRepo;
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
public class ShopHeaderService {
    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private ShopRepo shopRepo;

    public void saveShopHeader(ShopHero shopHero, MultipartFile imageFile) throws IOException {
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
            shopHero.setImageUrl(fileName);
        }
        shopRepo.save(shopHero);
    }

    public void DeleteShopHeader(Long id) {
        shopRepo.deleteById(id);
    }

    public List<ShopHero> findAllShopHeader() {
        return shopRepo.findAll();
    }
    public ShopHero findShopHeaderById(Long id) {
        return shopRepo.findById(id).
                orElseThrow(() -> new RuntimeException("Header with id " + id + " not found"));
    }

    public ShopHero updateShopHeader(ShopHero shopHero, MultipartFile imageFile) throws IOException {
        ShopHero existing = shopRepo.findById(shopHero.getId())
                .orElseThrow(() -> new RuntimeException("Shop header not found with id: " + shopHero.getId()));

        existing.setTitle(shopHero.getTitle());
        existing.setSubtext(shopHero.getSubtext());

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
        return shopRepo.save(existing);
    }
}
