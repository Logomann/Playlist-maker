package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.search.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}