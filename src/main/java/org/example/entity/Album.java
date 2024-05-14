package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "albums")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Album {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "image_file_URL")
    private String imageFileURL;

    @OneToMany(mappedBy = "album")
    @JsonIgnore
    private List<Audio> audios;

    @ManyToMany(mappedBy = "albums")
    @JsonIgnore
    private List<Artist> artists;
}
