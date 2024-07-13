package com.practicum.playlistmaker.data.db.impl

import com.practicum.playlistmaker.data.converters.TrackDbConverter
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.TrackEntity
import com.practicum.playlistmaker.domain.db.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.model.track.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConverter
) : FavoriteTracksRepository {

    override fun favoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun insertTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override fun isFavorite(trackId: Int): Flow<Boolean> = flow {
        val trackEntity = appDatabase.trackDao().getTrack(trackId)
        if (trackEntity != null) {
            emit(trackEntity.isFavorite)
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

}