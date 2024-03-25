package com.practicum.playlistmaker.data.track

import com.google.gson.Gson
import com.practicum.playlistmaker.domain.api.track.TrackRepository
import com.practicum.playlistmaker.domain.models.Track


class TrackRepositoryImpl : TrackRepository {
    override fun getTrackFromGson(gsonArray: String?): Track {
        return Gson().fromJson(gsonArray, Track::class.java)
    }
}