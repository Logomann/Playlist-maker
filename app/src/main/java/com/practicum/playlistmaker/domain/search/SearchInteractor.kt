package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.model.track.model.Track


interface SearchInteractor {
    fun loadNewTrackList(request: String, onComplete: (List<Track>?, String?) -> Unit)


}