package com.practicum.playlistmaker.data.search.impl

import androidx.activity.ComponentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.PREFERENCES
import com.practicum.playlistmaker.util.TRACK_KEY
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.search.SearchHistoryRepository

class SearchHistoryRepositoryImpl : SearchHistoryRepository {
    private val application = App.getApplication()
    private val sharedPreferences =
        application.getSharedPreferences(PREFERENCES, ComponentActivity.MODE_PRIVATE)

    private val listOfTracks = read()
    override fun loadSavedTrackList(): List<Track> {
        return read()
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
        return Gson().fromJson(json, type)
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
        val json = Gson().toJson(listOfTracks)
        sharedPreferences.edit()
            .putString(TRACK_KEY, json)
            .apply()
    }
}