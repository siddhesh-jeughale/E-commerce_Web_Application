package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.Testimonial;
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
public class TestimonialService {

    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private TestimonialRepo testimonialRepo;

    public void saveTestimonial(Testimonial testimonial, MultipartFile imageFile) throws IOException {
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
            testimonial.setImageUrl(fileName);
        }
        testimonialRepo.save(testimonial);
    }

    public void DeleteTestimonial(Long testimonialId) {
        if (testimonialId != null) {
            testimonialRepo.deleteById(testimonialId);
        }
    }
    public List<Testimonial> findAllTestimonial() {
        return testimonialRepo.findAll();
    }
    public Testimonial findTestimonialById(Long testimonialId) {
        return testimonialRepo.findById(testimonialId).
                orElseThrow(() -> new RuntimeException("Testimonial with id " + testimonialId + " not found"));
    }

    public Testimonial updateTestimonial(Testimonial testimonial, MultipartFile imageFile) throws IOException {
        Testimonial existing = testimonialRepo.findById(testimonial.getId())
                .orElseThrow(() -> new RuntimeException("Testimonial not found with id: " + testimonial.getId()));

        existing.setClientName(testimonial.getClientName());
        existing.setRole(testimonial.getRole());
        existing.setLocation(testimonial.getLocation());
        existing.setQuote(testimonial.getQuote());

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
        return testimonialRepo.save(existing);
    }

}
