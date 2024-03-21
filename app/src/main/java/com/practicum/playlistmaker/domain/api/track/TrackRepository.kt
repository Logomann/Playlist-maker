package com.practicum.playlistmaker.domain.api.track

import com.practicum.playlistmaker.domain.models.Track


interface TrackRepository {
    fun getTrackFromGson(gsonArray: String?): Track
}