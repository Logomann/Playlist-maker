package com.practicum.playlistmaker.ui.medialibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.medialibrary.FavoriteTracksInteractor
import com.practicum.playlistmaker.ui.medialibrary.FavoriteTracksScreenState

class FavoriteTracksViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor) :
    ViewModel() {

    private val screenStateLiveData =
        MutableLiveData<FavoriteTracksScreenState>(FavoriteTracksScreenState.Default)

    fun getFavoriteTracks(): LiveData<FavoriteTracksScreenState> {
        if (favoriteTracksInteractor.getFavoriteTracks().isEmpty()) {
            screenStateLiveData.postValue(FavoriteTracksScreenState.NoData)
        } else {
            screenStateLiveData.postValue(FavoriteTracksScreenState.Content(favoriteTracksInteractor.getFavoriteTracks()))
        }
        return screenStateLiveData
    }
}