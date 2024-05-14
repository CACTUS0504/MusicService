package org.example.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.entity.Artist;
import org.example.repository.ArtistRepository;
import org.example.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ArtistServiceImpl implements ArtistService {
    @Autowired
    ArtistRepository artistRepository;

    private String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now().toString());
    }
    @Override
    public String createArtist(String name, MultipartFile file) throws IOException {
        Artist artist = new Artist();

        String key = generateKey(file.getName());
        Path path = Paths.get("src/main/resources/static/img", key);
        Path fullPath = Files.createFile(path);

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(fullPath.toString());
            stream.write(file.getBytes());
        } finally {
            stream.close();
        }
        artist.setImageFileURL(path.toString());
        artist.setName(name);
        artistRepository.save(artist);

        return null;
    }

    @Override
    public Artist getArtist(String name) {
        return artistRepository.findByName(name);
    }

    @Override
    public void deleteArtist(String name) throws IOException {
        Artist artist = artistRepository.findByName(name);

        File artistImg = new File(artist.getImageFileURL());
        artistImg.delete();

        artistRepository.delete(artist);
    }

    @Override
    public List<Artist> getAllArtists() {
        List<Artist> artistsInfo = artistRepository.findAll();

        return artistsInfo;
    }
}
