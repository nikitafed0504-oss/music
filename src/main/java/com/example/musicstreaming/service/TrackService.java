package com.example.musicstreaming.service;

import com.example.musicstreaming.model.Track;
import com.example.musicstreaming.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;

    public List<Track> findAll() {
        return trackRepository.findAll();
    }

    public Optional<Track> findById(Long id) {
        return trackRepository.findById(id);
    }

    public Track save(Track track) {
        return trackRepository.save(track);
    }

    public Track update(Long id, Track trackDetails) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found with id: " + id));
        track.setTitle(trackDetails.getTitle());
        track.setDuration(trackDetails.getDuration());
        track.setArtist(trackDetails.getArtist());
        track.setAlbum(trackDetails.getAlbum());
        return trackRepository.save(track);
    }

    public void deleteById(Long id) {
        trackRepository.deleteById(id);
    }

    public List<Track> findByArtistId(Long artistId) {
        return trackRepository.findByArtistId(artistId);
    }

    public List<Track> findByAlbumId(Long albumId) {
        return trackRepository.findByAlbumId(albumId);
    }
}