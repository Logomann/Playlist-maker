package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.model.track.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun loadSavedTrackList(): List<Track>
    fun saveTrack(track: Track)
    fun clearList()
}