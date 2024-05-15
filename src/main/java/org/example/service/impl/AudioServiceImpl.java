package org.example.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.entity.Audio;
import org.example.repository.AlbumRepository;
import org.example.repository.AudioRepository;
import org.example.service.AlbumService;
import org.example.service.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AudioServiceImpl implements AudioService {
    @Autowired
    AudioRepository audioRepository;
    @Autowired
    AlbumServiceImpl albumService;
    @Autowired
    ArtistServiceImpl artistService;
    private String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now().toString());
    }

    @Override
    public String createAudio(String name, MultipartFile file, String albumName, List<String> artistNames)
            throws IOException {
        Album album = albumService.getAlbumByName(albumName, artistNames);
        List<Artist> artists = artistNames.stream().map(elem -> artistService.getArtist(elem)).toList();

        List<String> foundArtistNames = new ArrayList<>();
        artists.forEach((artist) -> {
            foundArtistNames.add(artist.getName());
        });

        List<String> albumArtistNames = new ArrayList<>();
        album.getArtists().forEach((artist) -> {
            albumArtistNames.add(artist.getName());
        });

        if (foundArtistNames.equals(albumArtistNames)) {
            String key = generateKey(file.getName());
            Path path = Paths.get("src/main/resources/static/music", key);
            Path fullPath = Files.createFile(path);
            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(fullPath.toString());
                stream.write(file.getBytes());
            } finally {
                stream.close();
            }
            Audio audio = new Audio();
            audio.setMusicFileURL(path.toString());
            audio.setName(name);
            audio.setAlbum(album);
            audio.setArtists(artists);

            // temp code
            artists.forEach(artist -> {
                List<Audio> artistAudios = artist.getAudios();
                artistAudios.add(audio);
                artist.setAudios(artistAudios);
            });
            List<Audio> albumAudios = album.getAudios();
            albumAudios.add(audio);
            album.setAudios(albumAudios);
            audioRepository.save(audio);
        }

//        String key = generateKey(file.getName());
//        Path path = Paths.get("src/main/resources/static/img", key);
//        Path fullPath = Files.createFile(path);
//
//        FileOutputStream stream = null;
//        try {
//            stream = new FileOutputStream(fullPath.toString());
//            stream.write(file.getBytes());
//        } finally {
//            stream.close();
//        }
//        artist.setImageFileURL(path.toString());
//        artist.setName(name);
//        artistRepository.save(artist);

        return null;
    }

    @Override
    public Artist getAudio(String name, String albumName) {
        return null;
    }

    @Override
    public void deleteAudio(String name, String albumName) throws IOException {

    }

    @Override
    public List<Audio> getAllAudios() {
        return null;
    }
}
