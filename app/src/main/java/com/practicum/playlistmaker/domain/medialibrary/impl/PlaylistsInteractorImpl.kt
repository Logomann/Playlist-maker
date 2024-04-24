package com.practicum.playlistmaker.domain.medialibrary.impl

import com.practicum.playlistmaker.domain.medialibrary.PlaylistsInteractor
import com.practicum.playlistmaker.domain.medialibrary.PlaylistsRepository
import com.practicum.playlistmaker.domain.model.Playlist

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) : PlaylistsInteractor {
    override fun getPlaylists(): List<Playlist> {
       return playlistsRepository.getPlayLists()
    }
}