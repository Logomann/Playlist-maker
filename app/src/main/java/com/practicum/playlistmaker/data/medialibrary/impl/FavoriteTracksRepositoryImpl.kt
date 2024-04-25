package com.practicum.playlistmaker.data.medialibrary.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.medialibrary.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.util.FAVORITE_TRACKS


class FavoriteTracksRepositoryImpl(private val sharedPreferences: SharedPreferences,
                                   private val gson: Gson
) : FavoriteTracksRepository {
    override fun getFavoriteTracks(): List<Track> {
        val json = sharedPreferences.getString(FAVORITE_TRACKS, null) ?: return ArrayList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type)
    }
}