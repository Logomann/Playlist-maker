package com.practicum.playlistmaker.domain.medialibrary

import com.practicum.playlistmaker.domain.model.track.model.Track

interface FavoriteTracksInteractor {
    fun getFavoriteTracks():List<Track>
}