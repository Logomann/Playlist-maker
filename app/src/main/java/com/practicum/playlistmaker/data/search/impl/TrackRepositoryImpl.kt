package com.practicum.playlistmaker.data.search.impl

import com.google.gson.Gson
import com.practicum.playlistmaker.domain.model.track.TrackRepository
import com.practicum.playlistmaker.domain.model.track.model.Track


class TrackRepositoryImpl(private val gson: Gson) : TrackRepository {
    override fun getTrackFromGson(gsonArray: String?): Track {
        return gson.fromJson(gsonArray, Track::class.java)
    }
}