package com.practicum.playlistmaker.ui.player

sealed class AudioPlayerScreenState {
    data object Completed : AudioPlayerScreenState()
    data object Default : AudioPlayerScreenState()
    data object Prepared : AudioPlayerScreenState()
    data class Playing(val progress: String) : AudioPlayerScreenState()
    data object Paused : AudioPlayerScreenState()
}