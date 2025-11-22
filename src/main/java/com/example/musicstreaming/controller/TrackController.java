package com.example.musicstreaming.controller;

import com.example.musicstreaming.model.Track;
import com.example.musicstreaming.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;

    @GetMapping
    public List<Track> getAllTracks() {
        return trackService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Track> getTrackById(@PathVariable Long id) {
        return trackService.findById(id)
                .map(track -> new ResponseEntity<>(track, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/artist/{artistId}")
    public List<Track> getTracksByArtist(@PathVariable Long artistId) {
        return trackService.findByArtistId(artistId);
    }

    @GetMapping("/album/{albumId}")
    public List<Track> getTracksByAlbum(@PathVariable Long albumId) {
        return trackService.findByAlbumId(albumId);
    }

    @PostMapping
    public ResponseEntity<Track> createTrack(@RequestBody Track track) {
        Track savedTrack = trackService.save(track);
        return new ResponseEntity<>(savedTrack, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Track> updateTrack(@PathVariable Long id, @RequestBody Track trackDetails) {
        try {
            Track updatedTrack = trackService.update(id, trackDetails);
            return new ResponseEntity<>(updatedTrack, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTrack(@PathVariable Long id) {
        try {
            trackService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}