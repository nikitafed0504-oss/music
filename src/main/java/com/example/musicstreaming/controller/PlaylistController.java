package com.example.musicstreaming.controller;

import com.example.musicstreaming.model.Playlist;
import com.example.musicstreaming.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @GetMapping
    public List<Playlist> getAllPlaylists() {
        return playlistService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long id) {
        return playlistService.findById(id)
                .map(playlist -> new ResponseEntity<>(playlist, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public List<Playlist> getPlaylistsByUser(@PathVariable Long userId) {
        return playlistService.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) {
        Playlist savedPlaylist = playlistService.save(playlist);
        return new ResponseEntity<>(savedPlaylist, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Long id, @RequestBody Playlist playlistDetails) {
        try {
            Playlist updatedPlaylist = playlistService.update(id, playlistDetails);
            return new ResponseEntity<>(updatedPlaylist, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePlaylist(@PathVariable Long id) {
        try {
            playlistService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<?> addTrackToPlaylist(@PathVariable Long playlistId, @PathVariable Long trackId) {
        try {
            Playlist updatedPlaylist = playlistService.addTrackToPlaylist(playlistId, trackId);
            return new ResponseEntity<>(updatedPlaylist, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<?> removeTrackFromPlaylist(@PathVariable Long playlistId, @PathVariable Long trackId) {
        try {
            playlistService.removeTrackFromPlaylist(playlistId, trackId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{playlistId}/tracks/{trackId}/reorder")
    public ResponseEntity<?> reorderTrackInPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId,
            @RequestParam Integer newPosition) {
        try {
            playlistService.reorderTracksInPlaylist(playlistId, trackId, newPosition);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}