package com.practicum.playlistmaker.ui.medialibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.medialibrary.PlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.ui.medialibrary.PlaylistsScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistListViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val screenStateLiveData =
        MutableLiveData<PlaylistsScreenState>(PlaylistsScreenState.Default)

    fun getPlaylists() {
        renderState(PlaylistsScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

    fun render(): LiveData<PlaylistsScreenState> {
        return screenStateLiveData
    }

    private fun renderState(state: PlaylistsScreenState) {
        screenStateLiveData.postValue(state)
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistsScreenState.NoData)
        } else {
            renderState(PlaylistsScreenState.Content(playlists))
        }
    }
}