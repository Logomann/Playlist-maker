package com.practicum.playlistmaker.data.search.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.util.TRACK_KEY
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.search.SearchHistoryRepository


class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val appDatabase: AppDatabase
) : SearchHistoryRepository {

    private val listOfTracks = read()

    override suspend fun loadSavedTrackList(): List<Track> {
        val favoriteList = appDatabase.trackDao().getTracksId()
        for (track in listOfTracks) {
            track.isFavorite = favoriteList.contains(track.trackId)
        }
        return listOfTracks
    }

    override fun saveTrack(track: Track) {
        addTrack(track)
    }

    override fun clearList() {
        clear()
    }

    private fun clear() {
        listOfTracks.clear()
        write()
    }

    private fun read(): ArrayList<Track> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return ArrayList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun addTrack(track: Track) {
        val iterator = listOfTracks.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().trackId == track.trackId) {
                iterator.remove()
            }
        }
        if (listOfTracks.size >= 10) {
            listOfTracks.removeAt(listOfTracks.lastIndex)
        }
        listOfTracks.add(0, track)
        write()
    }

    private fun write() {
        val json = gson.toJson(listOfTracks)
        sharedPreferences.edit {
            putString(TRACK_KEY, json)
        }
    }
}