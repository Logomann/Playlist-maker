package com.practicum.playlistmaker.domain.medialibrary.impl

import com.practicum.playlistmaker.domain.medialibrary.NewPlaylistInteractor
import com.practicum.playlistmaker.domain.medialibrary.NewPlaylistRepository
import com.practicum.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow


class NewPlaylistInteractorImpl(private val repository: NewPlaylistRepository) :
    NewPlaylistInteractor {


    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }

    override fun saveImage(filePath: String): Flow<String> {
        return repository.saveImage(filePath)
    }

}