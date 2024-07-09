package com.practicum.playlistmaker.data.medialibrary.impl


import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.data.converters.TrackDbConverter
import com.practicum.playlistmaker.data.converters.TracksInPlaylistsConverter
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.data.db.TrackEntity
import com.practicum.playlistmaker.domain.medialibrary.PlaylistsRepository
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.track.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val tracksInPlaylistsConverter: TracksInPlaylistsConverter,
    private val trackDbConverter: TrackDbConverter,
    private val context: Context
) : PlaylistsRepository {

    override fun getPlayLists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<String> = flow {
        appDatabase.tracksInPlaylistsDao().insertTrack(tracksInPlaylistsConverter.map(track))
        playlist.plTracksIDs.add(track.trackId)
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
        emit(context.getString(R.string.ok))
    }

    override fun getPlaylist(playlistId: Int): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistDao().getPlaylist(playlistId)
        emit(playlistDbConverter.map(playlist))
    }

    override fun getAddedTracks(listOfTracksId: List<Int>): Flow<List<Track>> = flow {
        val trackList = appDatabase.tracksInPlaylistsDao().getTracks()
            .map { track -> tracksInPlaylistsConverter.map(track) }
        val favoriteTracks = convertFromTrackEntity(appDatabase.trackDao().getTracks())
        trackList.forEach { track ->
            favoriteTracks.forEach {
                if (it.trackId == track.trackId) {
                    track.isFavorite = true
                }
            }
        }

        val listOfAddedTracks = mutableListOf<Track>()

        for (trackId in listOfTracksId) {
            trackList.forEach {
                if (it.trackId == trackId) {
                    listOfAddedTracks.add(it)
                }
            }
        }
        emit(listOfAddedTracks)
    }

    override fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Flow<Playlist> = flow {
        playlist.plTracksIDs.remove(track.trackId)
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
        checkTrackInPlaylists(track)
        emit(playlist)
    }

    override fun sharePlaylist(playlist: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, playlist)
        context.startActivity(
            Intent.createChooser(intent, context.getString(R.string.share_app))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun deletePlaylist(playlist: Playlist): Flow<String> = flow {
        appDatabase.playlistDao().deletePlaylist(playlistDbConverter.map(playlist))
        val listOfTracks = playlist.plTracksIDs
        for (it in listOfTracks) {
            val track =
                tracksInPlaylistsConverter.map(appDatabase.tracksInPlaylistsDao().getTrack(it))
            checkTrackInPlaylists(track)
        }
        emit(context.getString(R.string.ok))
    }

    private suspend fun checkTrackInPlaylists(track: Track) {
        val playlists = convertFromPlaylistEntity(appDatabase.playlistDao().getPlaylists())
        var isTrackInLists = false
        playlists.forEach {
            if (it.plTracksIDs.contains(track.trackId)) {
                isTrackInLists = true
            }
        }
        if (!isTrackInLists) {
            appDatabase.tracksInPlaylistsDao().deleteTrack(tracksInPlaylistsConverter.map(track))
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }

    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

}