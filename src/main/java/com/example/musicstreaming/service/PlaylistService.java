package com.example.musicstreaming.service;

import com.example.musicstreaming.model.*;
import com.example.musicstreaming.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;

    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }

    public Optional<Playlist> findById(Long id) {
        return playlistRepository.findById(id);
    }

    public List<Playlist> findByUserId(Long userId) {
        return playlistRepository.findByUserId(userId);
    }

    public Playlist save(Playlist playlist) {
        // Проверяем, что пользователь существует
        User user = userRepository.findById(playlist.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + playlist.getUser().getId()));
        playlist.setUser(user);
        return playlistRepository.save(playlist);
    }

    public Playlist update(Long id, Playlist playlistDetails) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));
        playlist.setName(playlistDetails.getName());
        return playlistRepository.save(playlist);
    }

    public void deleteById(Long id) {
        playlistRepository.deleteById(id);
    }

    @Transactional
    public Playlist addTrackToPlaylist(Long playlistId, Long trackId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        // Проверяем, не добавлен ли уже трек в плейлист
        if (playlistTrackRepository.findByPlaylistIdAndTrackId(playlistId, trackId).isPresent()) {
            throw new RuntimeException("Track already exists in playlist");
        }

        // Находим следующую доступную позицию
        List<PlaylistTrack> currentTracks = playlistTrackRepository.findByPlaylistIdOrderByPositionAsc(playlistId);
        int nextPosition = currentTracks.size();

        // Создаем новую связь
        PlaylistTrack playlistTrack = new PlaylistTrack();
        playlistTrack.setPlaylist(playlist);
        playlistTrack.setTrack(track);
        playlistTrack.setPosition(nextPosition);

        playlistTrackRepository.save(playlistTrack);
        return playlistRepository.findById(playlistId).orElseThrow();
    }

    @Transactional
    public void removeTrackFromPlaylist(Long playlistId, Long trackId) {
        PlaylistTrack playlistTrack = playlistTrackRepository.findByPlaylistIdAndTrackId(playlistId, trackId)
                .orElseThrow(() -> new RuntimeException("Track not found in playlist"));

        // Удаляем трек из плейлиста
        playlistTrackRepository.delete(playlistTrack);

        // Обновляем позиции оставшихся треков
        List<PlaylistTrack> remainingTracks = playlistTrackRepository.findByPlaylistIdOrderByPositionAsc(playlistId);
        for (int i = 0; i < remainingTracks.size(); i++) {
            PlaylistTrack pt = remainingTracks.get(i);
            pt.setPosition(i);
            playlistTrackRepository.save(pt);
        }
    }

    @Transactional
    public void reorderTracksInPlaylist(Long playlistId, Long trackId, Integer newPosition) {
        PlaylistTrack movingTrack = playlistTrackRepository.findByPlaylistIdAndTrackId(playlistId, trackId)
                .orElseThrow(() -> new RuntimeException("Track not found in playlist"));

        List<PlaylistTrack> allTracks = playlistTrackRepository.findByPlaylistIdOrderByPositionAsc(playlistId);

        Integer oldPosition = movingTrack.getPosition();

        if (newPosition < 0 || newPosition >= allTracks.size()) {
            throw new RuntimeException("Invalid position");
        }

        // Обновляем позиции
        if (newPosition < oldPosition) {
            // Перемещаем вверх - сдвигаем промежуточные треки вниз
            for (PlaylistTrack pt : allTracks) {
                if (pt.getPosition() >= newPosition && pt.getPosition() < oldPosition) {
                    pt.setPosition(pt.getPosition() + 1);
                    playlistTrackRepository.save(pt);
                }
            }
        } else {
            // Перемещаем вниз - сдвигаем промежуточные треки вверх
            for (PlaylistTrack pt : allTracks) {
                if (pt.getPosition() > oldPosition && pt.getPosition() <= newPosition) {
                    pt.setPosition(pt.getPosition() - 1);
                    playlistTrackRepository.save(pt);
                }
            }
        }

        movingTrack.setPosition(newPosition);
        playlistTrackRepository.save(movingTrack);
    }
}