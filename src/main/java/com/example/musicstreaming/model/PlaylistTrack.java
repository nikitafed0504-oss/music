package com.example.musicstreaming.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"playlist_id", "position"}),
        @UniqueConstraint(columnNames = {"playlist_id", "track_id"})
})
public class PlaylistTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonBackReference("playlist-tracks")
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    private Integer position;
}