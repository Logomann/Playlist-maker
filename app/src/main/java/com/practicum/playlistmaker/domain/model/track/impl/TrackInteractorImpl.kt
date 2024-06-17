package com.practicum.playlistmaker.domain.model.track.impl

import com.practicum.playlistmaker.domain.model.track.TrackInteractor
import com.practicum.playlistmaker.domain.model.track.TrackRepository
import com.practicum.playlistmaker.domain.model.track.model.Track


class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    override fun getLargeImageUrl(url: String): String {
        return url.replaceAfterLast('/', "512x512bb.jpg")
    }

    override fun getTrack(intentExtra: String?): Track {
        val track = repository.getTrackFromGson(intentExtra)
        return Track(
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
}