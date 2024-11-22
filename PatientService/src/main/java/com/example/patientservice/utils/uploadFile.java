package com.example.patientservice.utils;

import com.cloudinary.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Component
public class uploadFile {
    private final Cloudinary cloudinary;

    public  String upload(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
    }
}
