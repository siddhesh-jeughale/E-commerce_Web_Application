//package com.example.ArtistBackend.configuration;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.io.File;
//import java.util.Arrays;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Value("${artistry.upload.dir}")
//    private String uploadDir;
//
//
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/images/**")
////                .addResourceLocations("file:./uploads/");
//                .addResourceLocations("file:" + uploadDir + "/");
//    }
//
//    @PostConstruct
//    public void checkUploads() {
//        File uploadFolder = new File("./uploads");
//
//        System.out.println("Uploads path: " + uploadFolder.getAbsolutePath());
//        System.out.println("Uploads exists: " + uploadFolder.exists());
//
//        if (uploadFolder.exists()) {
//            System.out.println(Arrays.toString(uploadFolder.list()));
//        }
//    }
//}