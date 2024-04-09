package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.search.TrackSearchRequest
import com.practicum.playlistmaker.data.search.TrackSearchResponse
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.Resource


class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun searchTracks(query: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        val trackList = when (response.resultCode) {
            -1 -> {
                Resource.Error(null)
            }

            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
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

                })
            }

            else -> {
                Resource.Error("")
            }
        }
        return trackList

    }
}

