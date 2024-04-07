package com.practicum.playlistmaker.ui.search

import com.practicum.playlistmaker.domain.model.track.model.Track


sealed class SearchScreenState {
    data object Loading : SearchScreenState()
    data class Content(
        val data: List<Track>?,
        val message: String?
    ) : SearchScreenState()

    data object Default : SearchScreenState()
}