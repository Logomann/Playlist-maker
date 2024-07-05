package com.practicum.playlistmaker.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.domain.model.Playlist


class PlaylistDbConverter(private val gson: Gson) {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            plName = playlist.plName,
            plDescription = playlist.plDescription,
            plTracks = gson.toJson(playlist.plTracksIDs),
            plCover = playlist.plCover,
            plSize = playlist.plTracksIDs.size,
            plId = playlist.plId
        )
    }

    private fun listFromGson(list: String): MutableList<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(list, type)
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            plId = playlistEntity.plId,
            plName = playlistEntity.plName,
            plCover = playlistEntity.plCover,
            plDescription = playlistEntity.plDescription,
            plTracksIDs = listFromGson(playlistEntity.plTracks)
        )
    }
}