package com.practicum.playlistmaker.domain.model.track.model

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    var trackTimeMillis: String?,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
    var isFavorite: Boolean = false
)

