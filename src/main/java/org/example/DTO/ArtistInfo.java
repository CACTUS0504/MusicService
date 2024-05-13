package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.entity.Artist;

@AllArgsConstructor
@NoArgsConstructor
public class ArtistInfo {
    public String name;

    public ArtistInfo(Artist artist) {
        this.name = artist.getName();
    }
}
