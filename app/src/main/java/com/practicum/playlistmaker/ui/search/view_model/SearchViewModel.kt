package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.ui.search.SearchScreenState


class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    private var isCreated = false
    private var request = ""

    fun loadTrackList() {
        setScreenState(SearchScreenState.Loading)
        searchInteractor.loadNewTrackList(
            request = request,
            onComplete = { data, message ->
                screenStateLiveData.postValue(SearchScreenState.Content(data, message))
            })
    }

    fun isCreated(): Boolean {
        return isCreated
    }

    fun setCreated() {
        isCreated = true
    }

    fun getRequest(): String {
        return request
    }

    fun setRequest(request: String) {
        this.request = request
    }


    fun getScreenStateLiveData(): LiveData<SearchScreenState> = screenStateLiveData

    private fun setScreenState(state: SearchScreenState) {
        screenStateLiveData.postValue(state)
    }

    fun saveTrackToHistory(track: Track) {
        searchHistoryInteractor.saveTrack(track)
    }

    fun loadSavedTrackList(): List<Track> {
        return searchHistoryInteractor.loadSavedTrackList()
    }

    fun clearSavedTrackList() {
        searchHistoryInteractor.clearTrackList()
    }


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.provideSearchInteractor(),
                    Creator.provideSearchHistoryInteractor()
                )
            }
        }
    }
}