package com.practicum.playlistmaker.ui.medialibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.medialibrary.NewPlaylistInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.ui.medialibrary.EditPlaylistScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val interactor: NewPlaylistInteractor) :
    NewPlaylistViewModel(interactor) {

    private val screenStateLiveData = MutableLiveData<EditPlaylistScreenState>()

    fun renderState(): LiveData<EditPlaylistScreenState> {
        return screenStateLiveData
    }

    fun getData(json: String?) {
        val type = object : TypeToken<Playlist>() {}.type
        val playlist: Playlist = Gson().fromJson(json, type)
        process(playlist)

    }

    private fun process(playlist: Playlist) {
        screenStateLiveData.postValue(EditPlaylistScreenState.Content(playlist))
    }

    fun updatePlaylist(playlist: Playlist, name: String, description: String, cover: String?) {
        val list = Playlist(
            plId = playlist.plId,
            plName = name,
            plDescription = description,
            plTracksIDs = playlist.plTracksIDs,
            plCover = cover
        )

        viewModelScope.launch(Dispatchers.IO) {
            interactor.updatePlaylist(list)
        }

    }
}