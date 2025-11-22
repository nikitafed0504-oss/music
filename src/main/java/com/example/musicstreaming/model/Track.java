package com.example.musicstreaming.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    @JsonBackReference("artist-tracks")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "album_id")
    @JsonBackReference("album-tracks")
    private Album album;
}