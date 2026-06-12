package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.Awards;
import com.example.ArtistBackend.repository.AwardRepo;
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
import java.util.Optional;
import java.util.UUID;

@Service
public class AwardService {
    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private AwardRepo awardRepo;

    public Awards saveAwards(Awards awards, MultipartFile imageFile) throws IOException{
        if (imageFile != null && !imageFile.isEmpty()) {
//            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            String fileName = FileUploadUtil.generateFileName(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            awards.setImageUrl(fileName);

        }
        return awardRepo.save(awards);

    }
    public Awards getAwardById(Long awardId) throws Exception{
        return awardRepo.findById(awardId).orElseThrow(()->new Exception("Award not found"));
    }

    public void  deleteAwardsById(Long id){
        awardRepo.deleteById(id);
    }
    public List<Awards> findAllAwards(){
        return awardRepo.findAll();
    }
    public void updateAwards(Awards awards, MultipartFile imageFile) throws IOException{
        Awards existing = awardRepo.findById(awards.getId())
                .orElseThrow(() -> new RuntimeException("Award not found"));
        existing.setTitle(awards.getTitle());
        existing.setSubtitle(awards.getSubtitle());
        existing.setDescription(awards.getDescription());
        // awardDate = user-specified award date; do NOT touch createdDate (Hibernate-managed)
        existing.setAwardDate(awards.getAwardDate());

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
        awardRepo.save(existing);
    }
}
