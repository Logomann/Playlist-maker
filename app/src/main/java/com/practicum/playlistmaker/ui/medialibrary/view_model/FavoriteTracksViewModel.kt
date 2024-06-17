package com.practicum.playlistmaker.ui.medialibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.ui.medialibrary.FavoriteTracksScreenState
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor) :
    ViewModel() {

    private val screenStateLiveData =
        MutableLiveData<FavoriteTracksScreenState>()

    fun getFavoriteTracks() {
        renderState(FavoriteTracksScreenState.Loading)
        viewModelScope.launch {
            favoriteTracksInteractor
                .getFavoriteTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }

    }

    fun render(): LiveData<FavoriteTracksScreenState> = screenStateLiveData

    private fun renderState(state: FavoriteTracksScreenState) {
        screenStateLiveData.postValue(state)
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteTracksScreenState.NoData)
        } else {
            renderState(FavoriteTracksScreenState.Content(tracks))
        }
    }
}