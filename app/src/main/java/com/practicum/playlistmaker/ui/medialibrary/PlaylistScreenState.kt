package com.practicum.playlistmaker.ui.medialibrary

import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.track.model.Track

sealed class PlaylistScreenState {
    data class Content(val playlist: Playlist, val timeOfTracks: Int, val tracks: List<Track>) :
        PlaylistScreenState()

    data object Deleted : PlaylistScreenState()
}