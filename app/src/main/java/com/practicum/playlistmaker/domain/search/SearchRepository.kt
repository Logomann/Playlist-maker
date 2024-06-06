package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow


interface SearchRepository {
    fun searchTracks(query: String): Flow<Resource<List<Track>>>
}