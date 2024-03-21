package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track


class SearchHistory(private val preferences: SharedPreferences) {
    private val listOfTracks = read()

    fun addTrack(track: Track) {
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
        preferences.edit()
            .putString(TRACK_KEY, json)
            .apply()
    }

    fun read(): ArrayList<Track> {
        val json = preferences.getString(TRACK_KEY, null) ?: return ArrayList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun clear() {
        listOfTracks.clear()
        write()
    }
}
