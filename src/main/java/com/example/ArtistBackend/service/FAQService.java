package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.Exhibition;
import com.example.ArtistBackend.model.FAQ;
import com.example.ArtistBackend.repository.FAQRepo;
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
public class FAQService {

    @Value("${artistry.upload.dir:uploads/}")
    private String uploadDir;

    @Autowired
    private FAQRepo faqRepo;

    public FAQ saveFAQ(FAQ faq) throws IOException {
        return faqRepo.save(faq);
    }
    public FAQ getFAQById(Long faqId) throws Exception{
        return faqRepo.findById(faqId).orElseThrow(() -> new RuntimeException("FAQ not found with id: " + faqId));
    }

    public void  deleteFAQById(Long id){
        faqRepo.deleteById(id);
    }
    public List<FAQ> findAllFAQ(){
        return faqRepo.findAll();
    }
    public void updateFAQ(FAQ faq) throws IOException{
        FAQ existing = faqRepo.findById(faq.getId())
                .orElseThrow(() ->new RuntimeException("FAQ Not Found"));
        existing.setQuestion(faq.getQuestion());
        existing.setAnswer(faq.getAnswer());

        faqRepo.save(existing);
    }
}
