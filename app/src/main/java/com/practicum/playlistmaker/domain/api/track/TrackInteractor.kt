package com.practicum.playlistmaker.domain.api.track

import com.practicum.playlistmaker.domain.models.Track


interface TrackInteractor {
    fun getLargeImageUrl(url: String): String
    fun getTrack(intentExtra: String?): Track
}