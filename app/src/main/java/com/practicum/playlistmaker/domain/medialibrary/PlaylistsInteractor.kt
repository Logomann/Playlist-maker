package com.practicum.playlistmaker.domain.medialibrary

import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.track.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<String>
    fun getPlaylist(playlistId: Int): Flow<Playlist>
    fun getAddedTracks(listOfTracksId: List<Int>): Flow<List<Track>>
    fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Flow<Playlist>
    fun sharePlaylist(playlist: String)
    suspend fun deletePlaylist(playlist: Playlist)
}