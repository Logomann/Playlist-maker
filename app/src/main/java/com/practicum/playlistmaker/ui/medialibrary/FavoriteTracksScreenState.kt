package com.practicum.playlistmaker.ui.medialibrary

import com.practicum.playlistmaker.domain.model.track.model.Track


sealed class FavoriteTracksScreenState {
    data class Content(val data: List<Track>?) : FavoriteTracksScreenState()
    data object NoData : FavoriteTracksScreenState()
    data object Default : FavoriteTracksScreenState()
}