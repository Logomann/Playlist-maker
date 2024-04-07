package com.practicum.playlistmaker.domain.model.track

import com.practicum.playlistmaker.domain.model.track.model.Track


interface TrackInteractor {
    fun getLargeImageUrl(url: String): String
    fun getTrack(intentExtra: String?): Track
}