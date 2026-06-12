package com.example.ArtistBackend.util;

import java.util.UUID;

public class FileUploadUtil {

    public static String generateFileName(String originalFileName) {
        String extension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        return UUID.randomUUID() + extension;
    }
}