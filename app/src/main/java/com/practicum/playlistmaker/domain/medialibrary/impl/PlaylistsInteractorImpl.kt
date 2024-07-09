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

    override fun getPlaylist(playlistId: Int): Flow<Playlist> {
        return playlistsRepository.getPlaylist(playlistId)
    }

    override fun getAddedTracks(listOfTracksId: List<Int>): Flow<List<Track>> {
        return playlistsRepository.getAddedTracks(listOfTracksId)
    }

    override fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Flow<Playlist> {
        return playlistsRepository.deleteTrackFromPlaylist(track, playlist)
    }

    override fun sharePlaylist(playlist: String) {
        playlistsRepository.sharePlaylist(playlist)
    }

    override fun deletePlaylist(playlist: Playlist): Flow<String> {
        return playlistsRepository.deletePlaylist(playlist)
    }

}