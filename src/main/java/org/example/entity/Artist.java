package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "artists")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Artist {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "image_file_URL")
    private String imageFileURL;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "artist_audio",
            joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "audio_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<Audio> audios;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "artist_album",
            joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<Album> albums;
}
