package com.example.musicstreaming.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String genre;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    @JsonManagedReference("artist-tracks")
    private List<Track> tracks = new ArrayList<>();
}