package com.example.ArtistBackend.service;

import com.example.ArtistBackend.model.FAQ;
import com.example.ArtistBackend.model.Studio;
import com.example.ArtistBackend.repository.StudioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StudioService {

    @Autowired
    private StudioRepo studioRepo;

    public Studio saveStudio(Studio studio) throws IOException {
        return studioRepo.save(studio);
    }
    public Studio getStudioById(Long studioId) throws Exception{
        return studioRepo.findById(studioId).orElseThrow(() -> new RuntimeException("Studio not found with id: " + studioId));
    }

    public void  deleteStudioById(Long id){
        studioRepo.deleteById(id);
    }
    public List<Studio> findAllStudio(){
        return studioRepo.findAll();
    }
    public void updateStudio(Studio studio) throws IOException{
        Studio existing = studioRepo.findById(studio.getId())
                .orElseThrow(() ->new RuntimeException("studio Not Found"));
        existing.setVideoURL(studio.getVideoURL());
        existing.setTitle(studio.getTitle());
        existing.setDescription(studio.getDescription());

        studioRepo.save(existing);
    }
}
