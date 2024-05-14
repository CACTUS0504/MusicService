package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DTO.AlbumInfo;
import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.service.impl.AlbumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/album")
public class AlbumController {
    @Autowired
    private AlbumServiceImpl albumService;

    @PostMapping()
    public ResponseEntity creteAlbum(@RequestParam String name, @RequestParam MultipartFile file,
                                     @RequestParam String artistsNames) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<String> artistNamesList;
        try {
            artistNamesList = mapper.readValue(artistsNames, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        albumService.createAlbum(name, file, artistNamesList);
        return ResponseEntity.ok().body("Album created");
    }

    @PostMapping(path = "/img", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getAlbumImage(@RequestParam String name, @RequestParam String artistsNames)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<String> artistNamesList;
        try {
            artistNamesList = mapper.readValue(artistsNames, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Album album = albumService.getAlbumByName(name, artistNamesList);

        Path path = Paths.get(album.getImageFileURL());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok().body(resource);
    }

    @GetMapping("/all")
    public ResponseEntity getAlbums() {
        List<AlbumInfo> artists = albumService.getAllAlbums().stream().map(AlbumInfo::new).toList();
        return ResponseEntity.ok().body(artists);
    }

    @DeleteMapping()
    public ResponseEntity deleteAlbum(@RequestParam String name, @RequestParam String artistsNames) throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        List<String> artistNamesList;
        try {
            artistNamesList = mapper.readValue(artistsNames, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // album = albumService.getAlbumByName(name, artistNamesList);
        albumService.deleteAlbum(name, artistNamesList);

        return ResponseEntity.ok().body("Album deleted");
    }
}
