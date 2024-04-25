package com.practicum.playlistmaker.domain.medialibrary

import com.practicum.playlistmaker.domain.model.Playlist

interface PlaylistsRepository {
    fun getPlayLists():List<Playlist>
}