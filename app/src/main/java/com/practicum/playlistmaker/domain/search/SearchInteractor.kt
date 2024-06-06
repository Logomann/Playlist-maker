package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.model.track.model.Track
import kotlinx.coroutines.flow.Flow


interface SearchInteractor {
    fun loadNewTrackList(request: String): Flow<Pair<List<Track>?, String?>>

}