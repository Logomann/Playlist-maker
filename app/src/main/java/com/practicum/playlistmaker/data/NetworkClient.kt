package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.search.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}