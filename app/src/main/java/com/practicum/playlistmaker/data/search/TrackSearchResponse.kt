package com.practicum.playlistmaker.data.search


class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()