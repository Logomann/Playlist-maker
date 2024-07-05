package com.practicum.playlistmaker.ui.medialibrary

sealed class NewPlaylistScreenState {
    data class Content(val playlistName: String) : NewPlaylistScreenState()
    data object Default : NewPlaylistScreenState()
    data object Loading : NewPlaylistScreenState()
}