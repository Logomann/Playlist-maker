package com.practicum.playlistmaker.domain.medialibrary

import com.practicum.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow


interface NewPlaylistInteractor {
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun createPlaylist(playlist: Playlist)
    fun saveImage(filePath: String): Flow<String>
}