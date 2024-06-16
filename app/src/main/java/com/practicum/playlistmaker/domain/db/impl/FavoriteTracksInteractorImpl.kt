package com.practicum.playlistmaker.domain.db.impl

import com.practicum.playlistmaker.domain.db.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.db.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.model.track.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.favoriteTracks()
    }

    override suspend fun insertTrack(track: Track) {
        favoriteTracksRepository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteTracksRepository.deleteTrack(track)
    }
}