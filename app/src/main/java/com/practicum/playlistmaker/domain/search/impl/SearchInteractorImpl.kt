package com.practicum.playlistmaker.domain.search.impl


import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.Resource
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun loadNewTrackList(request: String, onComplete: (List<Track>?, String?) -> Unit) {
        executor.execute {
            when (val resource = repository.searchTracks(request)) {

                is Resource.Error -> {
                    onComplete(null, resource.message)
                }

                is Resource.Success -> {
                    onComplete(resource.data, null)
                }
            }
        }
    }

}