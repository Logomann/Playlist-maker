package com.practicum.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val plId: Int,
    val plName: String,
    val plDescription: String?,
    val plCover: String?,
    val plTracks: String,
    val plSize: Int
)
