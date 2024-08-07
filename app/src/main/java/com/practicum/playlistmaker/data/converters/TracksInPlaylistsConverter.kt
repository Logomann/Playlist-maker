package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.db.TracksInPlaylistsEntity
import com.practicum.playlistmaker.domain.model.track.model.Track

class TracksInPlaylistsConverter {
    fun map(track: Track): TracksInPlaylistsEntity {
        return TracksInPlaylistsEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite
        )
    }

    fun map(trackEntity: TracksInPlaylistsEntity): Track {
        return Track(
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl,
            trackEntity.isFavorite
        )
    }
}