package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.ui.search.SearchScreenState
import kotlinx.coroutines.launch


class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    private var request = ""
    private var isRefresh = false

    private fun loadTrackList() {
        setScreenState(SearchScreenState.Loading)
        viewModelScope.launch {
            searchInteractor
                .loadNewTrackList(request)
                .collect { data ->
                    if (data.first == null) {
                        screenStateLiveData.postValue(SearchScreenState.NoInternet)
                    } else if (data.first!!.isEmpty()) {
                        screenStateLiveData.postValue(SearchScreenState.NoData)
                    } else {
                        screenStateLiveData.postValue(SearchScreenState.Content(data.first, null))
                    }

                }
        }

    }

    fun setRefresh(isRefresh: Boolean) {
        this.isRefresh = isRefresh
    }

    fun clearData() {
        screenStateLiveData.postValue(SearchScreenState.Default)
        request = ""
    }


    fun setRequest(request: String) {
        if (request != this.request) {
            this.request = request
            loadTrackList()
        } else if (isRefresh) {
            loadTrackList()
            isRefresh = false
        }

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
}