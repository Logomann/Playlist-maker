package com.practicum.playlistmaker.domain.medialibrary.impl

import com.practicum.playlistmaker.domain.medialibrary.PlaylistsInteractor
import com.practicum.playlistmaker.domain.medialibrary.PlaylistsRepository
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.track.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    PlaylistsInteractor {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlayLists()
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<String> {
        return playlistsRepository.addTrackToPlaylist(track, playlist)
    }

}