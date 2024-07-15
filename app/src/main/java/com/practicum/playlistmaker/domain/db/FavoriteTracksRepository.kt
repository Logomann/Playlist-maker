package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.model.track.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    fun favoriteTracks(): Flow<List<Track>>
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun isFavorite(trackId: Int): Flow<Boolean>
}