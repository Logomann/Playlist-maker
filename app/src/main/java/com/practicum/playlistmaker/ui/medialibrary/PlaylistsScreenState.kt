package com.practicum.playlistmaker.ui.medialibrary

import com.practicum.playlistmaker.domain.model.Playlist

sealed class PlaylistsScreenState {
    data class Content(val playlist: List<Playlist>) : PlaylistsScreenState()
    data object Default : PlaylistsScreenState()
    data object NoData : PlaylistsScreenState()
    data object Loading : PlaylistsScreenState()
}