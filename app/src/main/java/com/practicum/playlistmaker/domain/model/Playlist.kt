package com.practicum.playlistmaker.domain.model

data class Playlist(
    val plId: Int,
    val plName: String,
    val plDescription: String?,
    var plCover: String?,
    val plTracksIDs: MutableList<Int>,
)