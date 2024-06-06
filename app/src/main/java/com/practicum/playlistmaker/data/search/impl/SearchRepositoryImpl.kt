package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.search.TrackSearchRequest
import com.practicum.playlistmaker.data.search.TrackSearchResponse
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(null))
            }

            200 -> {
                with(response as TrackSearchResponse) {
                    val data = results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
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

