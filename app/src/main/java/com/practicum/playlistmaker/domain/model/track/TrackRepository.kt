package com.practicum.playlistmaker.domain.model.track

import com.practicum.playlistmaker.domain.model.track.model.Track


interface TrackRepository {
    fun getTrackFromGson(gsonArray: String?): Track
}