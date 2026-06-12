package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.ContactHero;
import com.example.ArtistBackend.repository.ContactRepo;
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
public class ContactHeaderService {
    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private ContactRepo contactRepo;

    public void saveContactHeader(ContactHero contactHero, MultipartFile imageFile) throws IOException {
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
            contactHero.setImageUrl(fileName);
        }
        contactRepo.save(contactHero);
    }

    public void DeleteContactHeader(Long id) {
        contactRepo.deleteById(id);
    }

    public List<ContactHero> findAllContactHeader() {
        return contactRepo.findAll();
    }
    public ContactHero findContactHeaderById(Long id) {
        return contactRepo.findById(id).
                orElseThrow(() -> new RuntimeException("Header with id " + id + " not found"));
    }

    public ContactHero updateContactHeader(ContactHero contactHero, MultipartFile imageFile) throws IOException {
        ContactHero existing = contactRepo.findById(contactHero.getId())
                .orElseThrow(() -> new RuntimeException("Contact header not found with id: " + contactHero.getId()));

        existing.setTitle(contactHero.getTitle());
        existing.setSubtext(contactHero.getSubtext());

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
        return contactRepo.save(existing);
    }
}
