package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.search.TrackSearchRequest
import com.practicum.playlistmaker.data.search.TrackSearchResponse
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : SearchRepository {
    override fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(null))
            }

            200 -> {
                with(response as TrackSearchResponse) {
                    val favoriteTracks = appDatabase.trackDao().getTracksId()
                    val data = results.map {

                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(it.trackTimeMillis?.toFloat()),
                            it.artworkUrl100,
                            it.collectionName,
                            LocalDateTime.parse(
                                it.releaseDate ?: "0000-10-21T12:00:00Z",
                                DateTimeFormatter.ISO_DATE_TIME
                            ).year.toString(),
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    for (track in data) {
                        track.isFavorite = favoriteTracks.contains(track.trackId)
                    }
                    emit(Resource.Success(data))
                }

            }

            else -> {
                emit(Resource.Error(""))
            }
        }

    }
}

