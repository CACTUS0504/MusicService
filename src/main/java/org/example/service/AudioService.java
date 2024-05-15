package org.example.service;

import org.example.entity.Artist;
import org.example.entity.Audio;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AudioService {
    String createAudio(String name, MultipartFile file, String albumName, List<String> artistNames) throws IOException;
    Artist getAudio(String name, String albumName);
    void deleteAudio(String name, String albumName) throws IOException ;
    List<Audio> getAllAudios();
}
