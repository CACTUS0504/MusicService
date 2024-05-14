package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.entity.Album;
import org.example.entity.Artist;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class AlbumInfo {
    public String name;

    public List<String> artistNames;

    public AlbumInfo(Album album) {
        this.name = album.getName();
        this.artistNames = album.getArtists().stream().map(Artist::getName).toList();
    }
}
