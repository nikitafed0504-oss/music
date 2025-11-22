package com.example.musicstreaming.service;

import com.example.musicstreaming.model.Artist;
import com.example.musicstreaming.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Optional<Artist> findById(Long id) {
        return artistRepository.findById(id);
    }

    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }

    public Artist update(Long id, Artist artistDetails) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + id));
        artist.setName(artistDetails.getName());
        artist.setGenre(artistDetails.getGenre());
        return artistRepository.save(artist);
    }

    public void deleteById(Long id) {
        artistRepository.deleteById(id);
    }
}