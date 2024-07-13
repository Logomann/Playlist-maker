package com.practicum.playlistmaker.ui.medialibrary.view_model


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.medialibrary.NewPlaylistInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.ui.medialibrary.NewPlaylistScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(private val interactor: NewPlaylistInteractor) : ViewModel() {

    private var uri: Uri? = null
    private val screenStateLiveData = MutableLiveData<NewPlaylistScreenState>()

    fun render(): LiveData<NewPlaylistScreenState> {
        return screenStateLiveData
    }

    fun createPlaylist(name: String, description: String?, path: String?) {
        screenStateLiveData.postValue(NewPlaylistScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            interactor.createPlaylist(Playlist(0, name, description, path, mutableListOf()))
            screenStateLiveData.postValue(NewPlaylistScreenState.Content(name))
        }

    }

    fun setUri(uri: Uri) {
        this.uri = uri
    }

    fun getUri(): Uri? {
        return uri
    }

    fun saveImage(fileName: String, description: String?) {
        screenStateLiveData.postValue(NewPlaylistScreenState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            interactor
                .saveImage(uri.toString())
                .collect { path ->
                    createPlaylist(fileName, description, path)
                }
        }
    }


}