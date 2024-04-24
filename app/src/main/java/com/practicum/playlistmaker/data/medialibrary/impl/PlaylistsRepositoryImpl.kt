package com.practicum.playlistmaker.data.medialibrary.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.medialibrary.PlaylistsRepository
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.util.PLAYLISTS

class PlaylistsRepositoryImpl(private val sharedPreferences: SharedPreferences,
    private val gson:Gson) : PlaylistsRepository {
    override fun getPlayLists(): List<Playlist> {
        val json = sharedPreferences.getString(PLAYLISTS, null) ?: return ArrayList()
        val type = object : TypeToken<ArrayList<Playlist>>() {}.type
        return gson.fromJson(json, type)
    }
}