package org.example.service;

import org.example.entity.Album;
import org.example.entity.Artist;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AlbumService {
    String createAlbum(String name, MultipartFile image, List<String> artistNames) throws IOException;
    Album getAlbumByName(String albumName, List<String> artistNames);
    void deleteAlbum(String name, List<String> artistNames) throws IOException ;
    List<Album> getAllAlbums();
}
