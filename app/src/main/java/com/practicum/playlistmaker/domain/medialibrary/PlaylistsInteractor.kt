package com.practicum.playlistmaker.domain.medialibrary

import com.practicum.playlistmaker.domain.model.Playlist

interface PlaylistsInteractor {
    fun getPlaylists():List<Playlist>
}