package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.model.track.model.Track

interface SearchHistoryInteractor {
    fun loadSavedTrackList(): List<Track>
    fun saveTrack(track: Track)
    fun clearTrackList()
}