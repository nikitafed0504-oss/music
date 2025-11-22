package com.example.musicstreaming.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer releaseYear;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @JsonManagedReference("album-tracks")
    private List<Track> tracks = new ArrayList<>();
}