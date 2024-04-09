package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.util.Resource


interface SearchRepository {
    fun searchTracks(query: String): Resource<List<Track>>
}