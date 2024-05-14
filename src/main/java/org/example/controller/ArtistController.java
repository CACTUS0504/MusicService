package org.example.controller;

import org.example.DTO.ArtistInfo;
import org.example.entity.Artist;
import org.example.service.ArtistService;
import org.example.service.impl.ArtistServiceImpl;
import org.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/api/artist")
public class ArtistController {

    @Autowired
    private ArtistServiceImpl artistService;

    @PostMapping()
    public void creteArtist(@RequestParam String name, @RequestParam MultipartFile file) throws IOException {
        artistService.createArtist(name, file);
    }

    @GetMapping(path = "/img", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getArtistImage(@RequestParam String name) throws IOException{
        Artist artist = artistService.getArtist(name);

        Path path = Paths.get(artist.getImageFileURL());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok().body(resource);
    }

    @GetMapping("/all")
    public ResponseEntity getArtists() {
        List<ArtistInfo> artists = artistService.getAllArtists().stream().map(ArtistInfo::new).toList();
        return ResponseEntity.ok().body(artists);
    }

    @DeleteMapping()
    public ResponseEntity deleteArtist(@RequestParam String name) throws IOException{
        artistService.deleteArtist(name);
        return ResponseEntity.ok().body("Artist deleted");
    }
}
