package org.example.service;

import org.example.entity.Artist;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ArtistService {
    String createArtist(String name, MultipartFile image) throws IOException;
    Artist getArtist(String name);
    void deleteArtist(String name) throws IOException ;
    List<Artist> getAllArtists();
}