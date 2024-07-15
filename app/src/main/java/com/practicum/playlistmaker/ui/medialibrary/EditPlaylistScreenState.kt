package com.practicum.playlistmaker.ui.medialibrary

import com.practicum.playlistmaker.domain.model.Playlist

sealed class EditPlaylistScreenState {
    data class Content(val playlist: Playlist) : EditPlaylistScreenState()
}