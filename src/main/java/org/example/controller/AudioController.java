package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.impl.AudioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/api/audio")
public class AudioController {
    @Autowired
    AudioServiceImpl audioService;
    @GetMapping("/mp3/play")
    public StreamingResponseBody getFile() throws
            IOException, FileNotFoundException {


        File music = new File("src/main/resources/minecraft cave sound.mp3");

        final InputStream audioFileStream = new FileInputStream(music);

        return ( os) -> {
            readAndWrite(audioFileStream, os);
        };
    }
    private void readAndWrite(final InputStream is, OutputStream os)
            throws IOException {
        byte[] data = new byte[2048];
        int read = 0;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }


    @PostMapping()
    public ResponseEntity<String> creteAudio(@RequestParam String name, @RequestParam MultipartFile file,
                                             @RequestParam String albumName, @RequestParam String artistsNames) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> artistNamesList;
        try {
            artistNamesList = mapper.readValue(artistsNames, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        audioService.createAudio(name, file, albumName, artistNamesList);
        return ResponseEntity.ok().body("Audio created");
    }
}
