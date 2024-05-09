package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

@Controller
@RequestMapping("/api/audio")
public class AudioController {
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
}
