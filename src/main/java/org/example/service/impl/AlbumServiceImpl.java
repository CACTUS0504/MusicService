package org.example.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.repository.AlbumRepository;
import org.example.repository.ArtistRepository;
import org.example.service.AlbumService;
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
import java.util.List;


@Service
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    private String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now().toString());
    }

    @Override
    public String createAlbum(String name, MultipartFile file, List<String> artistNames) throws IOException {
        List<Artist> artists = artistNames.stream().map(artistName -> artistRepository.findByName(artistName)).toList();


        Album album = new Album();

        String key = generateKey(file.getName());
        Path path = Paths.get("src/main/resources/static/img/album", key);
        Path fullPath = Files.createFile(path);

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(fullPath.toString());
            stream.write(file.getBytes());
        } finally {
            stream.close();
        }
        album.setImageFileURL(path.toString());
        album.setName(name);
        album.setArtists(artists);

        // temp code
        artists.forEach(artist -> {
            List<Album> albums = artist.getAlbums();
            albums.add(album);
            artist.setAlbums(albums);
        });
        albumRepository.save(album);
        return null;
    }

    // Получаю все альбомы с заданным именем и ищу среди них альбом с нужными исполнителями
    @Override
    public Album getAlbumByName(String albumName, List<String> artistNames) {
        List<Album> foundAlbums = albumRepository.findAllByName(albumName);
        //System.out.println(foundAlbums.get(0).getName());

        for (Album foundAlbum : foundAlbums) {
            List<Artist> artists = foundAlbum.getArtists();
            List<String> foundArtistNames = new ArrayList<>();
            artists.forEach((artist) -> {
                foundArtistNames.add(artist.getName());
            });
            if (foundArtistNames.equals(artistNames)) return foundAlbums.get(0);
        }
        return null;
    }

    @Override
    public void deleteAlbum(String albumName, List<String> artistNames) throws IOException {
        List<Album> foundAlbums = albumRepository.findAllByName(albumName);

        for (Album foundAlbum : foundAlbums) {
            List<Artist> artists = foundAlbum.getArtists();
            List<String> foundArtistNames = new ArrayList<>();
            artists.forEach((artist) -> {
                foundArtistNames.add(artist.getName());
            });

            // Удаляю упоминания о альбоме у всех артистов, а затем удаляю сам альбом
            if (foundArtistNames.equals(artistNames)) {
                List<Artist> foundArtists = foundAlbums.get(0).getArtists();
                foundArtists.forEach((artist) -> {
                    List<Album> artistAlbums = artist.getAlbums();
                    artistAlbums.remove(foundAlbums.get(0));
                    artist.setAlbums(artistAlbums);
                });

                File albumImg = new File(foundAlbums.get(0).getImageFileURL());
                albumImg.delete();
                albumRepository.delete(foundAlbums.get(0));
            }
        }
    }

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albums = albumRepository.findAll();
        return albums;
    }
}
