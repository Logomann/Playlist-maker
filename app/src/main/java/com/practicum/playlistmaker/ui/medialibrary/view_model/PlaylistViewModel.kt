package com.practicum.playlistmaker.ui.medialibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.medialibrary.PlaylistsInteractor
import com.practicum.playlistmaker.ui.medialibrary.PlaylistsScreenState

class PlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val screenStateLiveData =
        MutableLiveData<PlaylistsScreenState>(PlaylistsScreenState.Default)

    fun getPlaylists(): LiveData<PlaylistsScreenState> {
        if (playlistsInteractor.getPlaylists().isEmpty()) {
            screenStateLiveData.postValue(PlaylistsScreenState.NoData)
        } else {
            screenStateLiveData.postValue(PlaylistsScreenState.Content(playlistsInteractor.getPlaylists()))
        }
        return screenStateLiveData
    }
}