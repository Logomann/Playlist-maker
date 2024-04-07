package com.practicum.playlistmaker.domain.model.track.impl

import com.practicum.playlistmaker.domain.model.track.TrackInteractor
import com.practicum.playlistmaker.domain.model.track.TrackRepository
import com.practicum.playlistmaker.domain.model.track.model.Track
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
            SimpleDateFormat(
                "mm:ss", Locale.getDefault()
            ).format(track.trackTimeMillis?.toFloat()),
            track.artworkUrl100,
            track.collectionName,
            LocalDateTime.parse(track.releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString(),
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}