package com.example.patientservice.config;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    private Dotenv dotenv;

    public CloudinaryConfig() {
        try {
            dotenv = Dotenv.load();
        } catch (Exception e) {
            System.err.println("Failed to load .env file");
            e.printStackTrace();
        }
    }

    @Bean
    public Cloudinary cloudinary() {
        Map<String,String> config = new HashMap<>();
        String CLOUD_NAME = dotenv.get("CLOUD_NAME");
        String API_KEY =  dotenv.get("API_KEY");
        String API_SECRET = dotenv.get("API_SECRET") ;
        try {
            config.put("cloud_name", CLOUD_NAME);
            config.put("api_key", API_KEY);
            config.put("api_secret", API_SECRET);
        } catch (Exception e) {
            System.err.println("Failed to load environment variables");
            e.printStackTrace();
        }
        return new Cloudinary(config);
    }
}