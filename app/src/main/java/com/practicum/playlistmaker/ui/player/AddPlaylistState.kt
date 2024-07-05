package com.practicum.playlistmaker.ui.player

sealed class AddPlaylistState {
    data class Added(val playlistName: String) : AddPlaylistState()
    data class AlreadyExist(val playlistName: String) : AddPlaylistState()
}