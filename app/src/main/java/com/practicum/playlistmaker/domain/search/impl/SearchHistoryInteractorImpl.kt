package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override suspend fun loadSavedTrackList(): List<Track>  {
        return repository.loadSavedTrackList()
    }

    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override fun clearTrackList() {
        repository.clearList()
    }
}