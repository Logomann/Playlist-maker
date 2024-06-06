package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.data.search.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun findTrack(@Query("term") text: String): TrackSearchResponse
}