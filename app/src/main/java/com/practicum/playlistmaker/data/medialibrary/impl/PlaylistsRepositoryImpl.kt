package com.practicum.playlistmaker.data.medialibrary.impl


import com.practicum.playlistmaker.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.data.converters.TracksInPlaylistsConverter
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.domain.medialibrary.PlaylistsRepository
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.track.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val tracksInPlaylistsConverter: TracksInPlaylistsConverter
) : PlaylistsRepository {

    override fun getPlayLists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<String> = flow {
        appDatabase.tracksInPlaylistsDao().insertTrack(tracksInPlaylistsConverter.map(track))
        playlist.plTracksIDs.add(track.trackId)
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
        emit("Ok")
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }

    }
}