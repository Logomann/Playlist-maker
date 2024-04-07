package com.practicum.playlistmaker.data.search.impl

import com.google.gson.Gson
import com.practicum.playlistmaker.domain.model.track.TrackRepository
import com.practicum.playlistmaker.domain.model.track.model.Track


class TrackRepositoryImpl : TrackRepository {
    override fun getTrackFromGson(gsonArray: String?): Track {
        return Gson().fromJson(gsonArray, Track::class.java)
    }
}