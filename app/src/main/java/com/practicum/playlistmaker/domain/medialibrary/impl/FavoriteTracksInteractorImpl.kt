package com.practicum.playlistmaker.domain.medialibrary.impl

import com.practicum.playlistmaker.domain.medialibrary.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.medialibrary.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.model.track.model.Track

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) : FavoriteTracksInteractor {
    override fun getFavoriteTracks(): List<Track> {
      return  favoriteTracksRepository.getFavoriteTracks()
    }
}