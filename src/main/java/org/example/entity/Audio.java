package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "audios")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Audio {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "music_file_URL")
    private String musicFileURL;

    @ManyToMany(mappedBy = "audios")
    @JsonIgnore
    private List<User> users;

    @ManyToMany(mappedBy = "audios")
    @JsonIgnore
    private List<Artist> artists;

    @ManyToOne
    @JoinColumn(name="album_id")
    @JsonIgnore
    private Album album;
}
