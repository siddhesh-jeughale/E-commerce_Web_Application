package com.example.ArtistBackend.service;


import com.example.ArtistBackend.model.Exhibition;
import com.example.ArtistBackend.repository.ExhibitionRepo;
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
public class ExhibitionService {
    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private ExhibitionRepo exhibitionRepo;

    public Exhibition saveExhibition(Exhibition exhibition, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
//            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            String fileName = FileUploadUtil.generateFileName(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            exhibition.setImageUrl(fileName);

        }
        return exhibitionRepo.save(exhibition);

    }
    public Exhibition getExhibitionById(Long exhibitionId) throws Exception{
        return exhibitionRepo.findById(exhibitionId).orElseThrow(()->new Exception("exhibition not found"));
    }

    public void  deleteExhibitionById(Long id){
        exhibitionRepo.deleteById(id);
    }
    public List<Exhibition> findAllExhibition(){
        return exhibitionRepo.findAll();
    }
    public void updateExhibition(Exhibition exhibition, MultipartFile imageFile) throws IOException{
        Exhibition existing = exhibitionRepo.findById(exhibition.getId())
                .orElseThrow(() ->new RuntimeException("Exhibition not found"));
        existing.setTitle(exhibition.getTitle());
       existing.setType(exhibition.getType());
       existing.setYearBadge(exhibition.getYearBadge());
       existing.setLocation(exhibition.getLocation());

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
        exhibitionRepo.save(existing);
    }
}
